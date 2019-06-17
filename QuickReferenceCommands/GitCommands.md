# Git Commands

Reference https://confluence.atlassian.com/bitbucketserver/basic-git-commands-776639767.html


*Configure the author name and email address to be used with your commits.
Note that Git strips some characters (for example trailing periods) from user.name.*


    git config --global user.name "Sathiyanarayanan"
    git config --global user.email "sathya@satcos.in"


*Clone an existing repository*

    git clone http://username@host:/path/to/repository.git

*Commit changes to head (but not yet to the remote repository)*

    git commit -m "Commit message"

*Send changes to the master branch of your remote repository:*

    git push origin master

*If you haven't connected your local repository to a remote server, add the server to be able to push to it*

    git remote add origin http://username@host:/path/to/repository.git

*Create a new branch and switch to it:*

    git checkout -b <newbranchname> origin/<branchname>

*Set up stream for an branch*

    git branch --set-upstream-to=origin/<branchname> <branchname>

*Switch from one branch to another:*

    git checkout <branchname>

*List all the branches in your repo, and also tell you what branch you're currently in:*

    git branch

*Delete the feature branch:*

    git branch -d <branchname>

*Push the branch to your remote repository, so others can use it:*

    git push origin <branchname>

*Fetch and merge changes on the remote server to your working directory:*

    git pull

*To merge a different branch into your active branch:*

    git merge <branchname>

*To remove a local pointer to remote branch(Don't use dry run to perform the action):*

    git remote prune origin --dry-run
