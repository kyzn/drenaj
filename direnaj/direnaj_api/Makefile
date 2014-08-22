clean:
	find . -name \*.pyc -delete
	find . -name '__pycache__' | xargs rm -rf
	find . -iname 'htmlcov' | xargs rm -rf
	find . -iname '.coverage' | xargs rm -rf

test:
	python tests/run.py

coverage:
	coverage run --source='.' tests/run.py

report: clean coverage
	@coverage html
	@echo ""
	@echo "DONE! Test report is ready at htmlcov/index.html. Open it with browser"
