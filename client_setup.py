__author__ = 'onur'

from setuptools import setup, find_packages

setup(name='direnaj_client',
      version='0.7',
      description='DIRENAJ client.',
      url='http://voltran.cmpe.boun.edu.tr',
      author='Onur Gungor',
      author_email='onurgu@gmail.com',
      packages=find_packages(exclude=['*direnaj_api*']),
      install_requires=file("env/env_client_requirements.txt").readlines(),
      include_package_data = True,
      entry_points = {
        'console_scripts': [
            'direnaj_client = direnaj.client_startup:main',
        ],
      },
      zip_safe=False)