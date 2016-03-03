/**
 * Copyright (C) 2011-2015 Incapture Technologies LLC
 *
 * This is an autogenerated license statement. When copyright notices appear below
 * this one that copyright supercedes this statement.
 *
 * Unless required by applicable law or agreed to in writing, software is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * Unless explicit permission obtained in writing this software cannot be distributed.
 */
package rapture.repo.jdbc;

import rapture.common.RaptureFolderInfo;
import rapture.common.RaptureNativeQueryResult;
import rapture.common.RaptureQueryResult;
import rapture.common.exception.RaptNotSupportedException;
import rapture.common.exception.RaptureException;
import rapture.common.exception.RaptureExceptionFactory;
import rapture.common.exception.RaptureExceptionFormatter;
import rapture.index.IndexProducer;
import rapture.index.IndexHandler;
import rapture.repo.KeyStore;
import rapture.repo.RepoLockHandler;
import rapture.repo.RepoVisitor;
import rapture.repo.StoreKeyVisitor;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * A JDBCKeyStore is a key store implemented on a set of relational tables.
 * <p/>
 * It is assumed that Rapture (and this add-in) owns the tables and no-one else
 * is going to manipulate them from left field.
 * <p/>
 * The table structure is as follows.
 * <p/>
 * PrimaryDataTable
 * <p/>
 * keyName VARCHAR(512) content TEXT (?)
 * <p/>
 * If folder handling is enabled a secondary table is created to support:
 * "give me the subkeys of this key" (files and folders)
 * "give me all of the subkeys below this point"
 * <p/>
 * type hier prefix depth count
 * <p/>
 * (See MongoDB FolderStructureStore for an example - a sub implementation
 * should follow this)
 * <p/>
 * If the tables do not exist they need to be created.
 *
 * @author amkimian
 */
public class JDBCKeyStore implements KeyStore {
    private static Logger log = Logger.getLogger(JDBCKeyStore.class);

    @Override
    public void resetFolderHandling() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean containsKey(String ref) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long countKeys() throws RaptNotSupportedException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public KeyStore createRelatedKeyStore(String relation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean delete(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteUpTo(String key, long millisTimestamp) {
        throw RaptureExceptionFactory.create(HttpURLConnection.HTTP_INTERNAL_ERROR, "Delete by timestamp is unsupported.");
    }

    @Override
    public boolean delete(List<String> keys) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean dropKeyStore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String get(String k) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String get(String k, long millisTimestamp) {
        throw RaptureExceptionFactory.create(HttpURLConnection.HTTP_INTERNAL_ERROR, "Get by timestamp is unsupported.");
    }

    @Override
    public List<String> getBatch(List<String> keys) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStoreId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void put(String k, String v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void put(String k, long millisTimestamp, String v) {
        throw RaptureExceptionFactory.create(HttpURLConnection.HTTP_INTERNAL_ERROR, "Put with timestamp is unsupported.");
    }

    @Override
    public RaptureQueryResult runNativeQuery(String repoType, List<String> queryParams) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RaptureNativeQueryResult runNativeQueryWithLimitAndBounds(String repoType, List<String> queryParams, int limit, int offset) {
        // TODO Auto-generated method stub
        return null;
    }

    private String url;
    private String password;
    private String user;
    private Connection connection;
    @SuppressWarnings("unused")
    private String prefix;

    @Override
    public void setConfig(Map<String, String> config) {
        // The config will give us the connection information and the
        // tableName prefix. At this point we
        // need to initialize the table(s) if they do not exist.
        log.info("Initializing JDBCSqlStore");
        url = config.get("url");
        user = config.get("user");
        password = config.get("password");
        prefix = config.get("prefix");
        log.info("Attempting to connect to the database through " + url);
        // Create a connection to the database
        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection == null) {
                log.error("A null connection was returned");
            }
        } catch (SQLException e) {
            RaptureException raptException = RaptureExceptionFactory.create(HttpURLConnection.HTTP_INTERNAL_ERROR, "Error connecting to the database");
            log.error(RaptureExceptionFormatter.getExceptionMessage(raptException, e));
            throw raptException;
        }
        log.info("Database connection made successfully");
        checkTables();
    }

    private void checkTables() {
        // Do the tables exist? If not, create them
        try {
            DatabaseMetaData meta = connection.getMetaData();
            meta.getTables(null, null, null, new String[] { "TABLE" });
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void setInstanceName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(String folderPrefix, RepoVisitor iRepoVisitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitKeys(String prefix, StoreKeyVisitor iStoreKeyVisitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitKeysFromStart(String startPoint, StoreKeyVisitor iStoreKeyVisitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<RaptureFolderInfo> getSubKeys(String prefix) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean matches(String key, String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<String> getAllSubKeys(String displayNamePart) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndexHandler createIndexHandler(IndexProducer indexProducer) {
        // TODO If we are able to create a perspective table store, create one in a standard way based on this
        // config.
        return null;
    }

    @Override
    public List<RaptureFolderInfo> removeSubKeys(String folder, Boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean validate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long getSize() {
        return -1L;
    }

    @Override
    public void setRepoLockHandler(RepoLockHandler repoLockHandler) {

    }

    @Override
    public boolean supportsVersionLookupByTime() {
        return false;
    }
}