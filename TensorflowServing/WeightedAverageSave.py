"""This script will train and export a linear regression model into .pb, which 
will is to be served by tensorflow serving. 
"""

import os
import sys

import tensorflow as tf
import numpy as np 

tf.app.flags.DEFINE_integer('model_version', 1, 'version number of the model.')
tf.app.flags.DEFINE_string('work_dir', '/home/satcos/ModelStore/WeightedAverage', 'Working directory.')
FLAGS = tf.app.flags.FLAGS

def main(_):  
    sess = tf.InteractiveSession()

    x = tf.placeholder('float', shape=[None, 3])
    weights = np.array([1, 2, 3], dtype=np.float32).reshape((3, 1))
    w = tf.constant(weights)
    sess.run(tf.global_variables_initializer())
    y = tf.matmul(x, w) / 3
    train_x = np.random.randn(10, 3)
    output = sess.run(y, {x:train_x})
    print('Done training!')
    
    # Export model
    export_path_base = FLAGS.work_dir
    export_path = os.path.join(tf.compat.as_bytes(export_path_base), tf.compat.as_bytes(str(FLAGS.model_version)))
    print('Exporting trained model to', export_path)
    builder = tf.saved_model.builder.SavedModelBuilder(export_path)

    tensor_info_x = tf.saved_model.utils.build_tensor_info(x)
    tensor_info_y = tf.saved_model.utils.build_tensor_info(y)

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

