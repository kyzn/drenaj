__author__ = 'onur'

from setuptools import setup, find_packages

setup(name='drenaj_client',
      version='0.8.2',
      description='DRENAJ client.',
      url='http://voltran.cmpe.boun.edu.tr',
      author='Onur Gungor',
      author_email='onurgu@gmail.com',
      packages=find_packages(exclude=['*drenaj_api*']),
      install_requires=file("env/env_client_requirements.txt").readlines(),
      include_package_data = True,
      entry_points = {
        'console_scripts': [
            'drenaj_client = drenaj.client_startup:main',
        ],
      },
      zip_safe=False)