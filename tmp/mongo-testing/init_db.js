load('config.js');

load('aux.js');

var n_collections = backup_db.stats().collections;

print('DB design type ', db_design_type);

if (n_collections === 0) {
    // init
    backup_db = init_db(backup_db, db_design_type);
    print('Copying database');
    var backup_db = conn.getDB(backup_db_name);
    var db = conn.getDB(db_name);
    print(db);
    db.dropDatabase();
    db.copyDatabase(backup_db_name, db_name);
} else {
    if (reset_db) {
        db.dropDatabase();
        print('Loading database');
        db.copyDatabase(backup_db_name, db_name);
    }
}

