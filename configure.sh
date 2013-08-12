sudo apt-get install pip

sudo pip install virtualenvwrapper

mkvirtualenv direnaj
setvirtualenvproject

#m set the project root dir
export PROJECT_ROOT_DIR=`pwd` && sed "s@^\(PROJECT_ROOT_DIR=\).*@\1'$PROJECT_ROOT_DIR'@g" src/config.py.tmpl > src/config.py
