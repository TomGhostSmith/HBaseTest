import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class HBase
{
    public class Result
    {

    }
    public class Get{}
    public class Put{}
    public class ResultScanner{}
    public class Scan{}
    public class Region{}
    public class Key{}
    
    
    
    public class HTable
    {
        List<Region> regions;

        public HTable()
        {
            regions = new ArrayList<>();
        }

        // for public 
        Result get(Get get)  // for single row search
        {
            // may need search all HRegion or use hash
            return null;
        }
        void put(List<Put> puts);
        // ResultScanner scan(Scan scan);  // may not use
        void close();
    
        // for internal
        void addRegion();
        Region getRegion();
        void removeRegion();
    
    }
    
    public class HRegion
    {
        List<Store> stores;

        public HRegion()
        {
            stores = new ArrayList<>();
        }

        // for public
        Result get(Get get)
        {
            Result result = new Result();
            for (Store store:stores)
            {
                result.append(store.get(get));
            }
            return result;
        }

        void put(Put put)
        {
            String storeName = put.getStoreName();
            Store store = stores.get(storeName);
            store.put(put);
        }

        
        // for internal
        void setStore();
    }
    
    public class Store
    {
        List<StoreFile> storeFiles;
        MemStore memStore;

        public Store()
        {

        }

        public Result get(Get get)
        {
            Result result = memStore.get(get);
            if (result == null)
            {
                // result = new Result();
                for (StoreFile storeFile:storeFiles)
                {
                    result = storeFile.get(get);
                    if (result != null)
                    {
                        break;
                    }
                }
            }
        }

        public void put(Put put)
        {
            memStore.put(put);
        }

        public void dump()
        {
            // find corresponding storeFile
            for (KeyValue record:records)
            {
                StoreFile storeFile = findStoreFile();
                storeFile.put(record);
            }
        }
    }
    
    public class MemStore
    {
        List<KeyValue> records;

        public MemStore()
        {
            records = new ArrayList<>();
        }

        public void put(Put put)
        {
            KeyValue keyValue = put.getKeyValue();
            records.add(keyValue);
        }

        public Result get(Get get)
        {
            Result result = null;
            Key key = get.getKey();
            for (KeyValue record:records)
            {
                if (record.getKey().equals(key))
                {
                    result = record.getResult();
                }
            }
            return result;
        }
    }
    
    public class StoreFile
    {
        HFile file;

        public void readFile(Get get)
        {
            file = HDFS.read(get);
        }

        

    }
    
    
    public class Client
    {
        
    }

    public class KeyValue extends HashMap
    {
        Key key;
        HashMap<String, Object> values;

    }
}

