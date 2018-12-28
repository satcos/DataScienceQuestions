"""This script will train and export a Logitstic regression model into .pb, which 
will is to be served by tensorflow serving. 
"""

import os
import sys

import tensorflow as tf
import numpy as np 

tf.app.flags.DEFINE_integer('training_iteration', 10, 'number of training iterations.')
tf.app.flags.DEFINE_integer('model_version', 1, 'version number of the model.')
tf.app.flags.DEFINE_integer('numVariable', 100, 'Number of variables in the model.')
tf.app.flags.DEFINE_string('work_dir', '/home/satcos/TFSMLSFW/ModelStore/LogisticRegression', 'Working directory.')
FLAGS = tf.app.flags.FLAGS

def parseData():
    train_path = "logisticRegressionData.csv"
    trainData = np.genfromtxt(train_path, delimiter=',', dtype=np.float32, skip_header=1)
    records = trainData[:, :FLAGS.numVariable]
    labels = trainData[:, -1:].astype(np.int32)
    # records = np.random.uniform(0.5, 250.0, size=(50000, 3))
    # labels = np.random.randint(0, 2, size=(50000, 1))
    labels = np.hstack((labels, 1 - labels))
    return (records, labels)

def main(_):

    sess = tf.InteractiveSession()

    x = tf.placeholder(tf.float32, shape=[None, FLAGS.numVariable])
    y = tf.placeholder(tf.int32, shape=[None, 2])
    w = tf.get_variable('w', shape = [FLAGS.numVariable, 2], initializer = tf.truncated_normal_initializer)
    b = tf.get_variable('b', shape = [2], initializer = tf.zeros_initializer)
    sess.run(tf.global_variables_initializer())
    
    # Calculate unnormalized target estimates
    y_hat = tf.matmul(x, w) + b

    # Optimize with SGD
    loss = tf.losses.softmax_cross_entropy(y, y_hat)
    optimizer = tf.train.GradientDescentOptimizer(0.01)
    
    # Operations for training and inference
    optimize_op = optimizer.minimize(loss)
    predict_op = tf.nn.softmax(y_hat)

    # Read data from file
    train_x, train_y = parseData()
    # Train the model
    for _ in range(FLAGS.training_iteration):
        _, loss_train = sess.run([optimize_op, loss], feed_dict={x: train_x, y: train_y})
        print('Training loss %g' % loss_train)
    print('Done training!')

    # Export model
    export_path_base = FLAGS.work_dir
    export_path = os.path.join(tf.compat.as_bytes(export_path_base), tf.compat.as_bytes(str(FLAGS.model_version)))
    print('Exporting trained model to', export_path)
    builder = tf.saved_model.builder.SavedModelBuilder(export_path)

    tensor_info_x = tf.saved_model.utils.build_tensor_info(x)
    tensor_info_y = tf.saved_model.utils.build_tensor_info(predict_op)

    prediction_signature = (
        tf.saved_model.signature_def_utils.build_signature_def(
          inputs={'input': tensor_info_x},
          outputs={'output': tensor_info_y},
          method_name=tf.saved_model.signature_constants.PREDICT_METHOD_NAME))

    legacy_init_op = tf.group(tf.tables_initializer(), name='legacy_init_op')
    builder.add_meta_graph_and_variables(
        sess, [tf.saved_model.tag_constants.SERVING],
        signature_def_map={
          'prediction':
              prediction_signature,
      },
      legacy_init_op=legacy_init_op)
    builder.save()

    print('Done exporting!')


if __name__ == '__main__':
    tf.app.run()

