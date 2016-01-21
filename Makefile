all: clean build_sdist

clean:
	rm -rf drenaj_client.egg-info/ dist/

build_sdist:
	python client_setup.py sdist


