package kidouchi.chronobook;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

/**
 * Created by iuy407 on 12/8/15.
 */
public class RealmMigration implements io.realm.RealmMigration {
    @Override
    public void migrate(DynamicRealm dynamicRealm, long oldVersion, long newVersion) {

        RealmSchema schema = dynamicRealm.getSchema();

        if (newVersion == 3) {
//            schema.get("Event")
        }
    }
}
