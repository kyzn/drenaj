load('config.js');

load('aux.js');


var n_collections = backup_db.stats().collections;

if (n_collections === 0) {
    // init
    db = init_db(backup_db, db_design_type);
} else {
    db.dropDatabase();
    print('Loading database');
    db.copyDatabase(backup_db_name, db_name);
}

