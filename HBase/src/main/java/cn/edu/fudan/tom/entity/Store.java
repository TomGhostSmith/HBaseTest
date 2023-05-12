package cn.edu.fudan.tom.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Store
{
    private int minColumnHash;
    private int maxColumnHash;

    private MemStore memStore;
    private List<StoreFile> storeFiles;

    public Store(int minColumnHash, int maxColumnHash)
    {
        this.memStore = new MemStore();
        this.storeFiles = new ArrayList<>();
        this.minColumnHash = minColumnHash;
        this.maxColumnHash = maxColumnHash;
    }

    public boolean contain(String columnName)
    {
        int hashCode = columnName.hashCode();
        return hashCode >= minColumnHash && hashCode < maxColumnHash;
    }

    public Value search(Key key, String columnName)
    {
        Value result = memStore.search(key, columnName);
        if (result == null)
        {
            for (StoreFile storeFile:storeFiles)
            {
                result = storeFile.search(key, columnName);
                if (result != null)
                {
                    break;
                }
            }
        }
        return result;
    }

    public void modify(Key key, Value value)
    {
        // if exists, then modify. if not, add new one.
        if (!memStore.existsKey(key))
        {
            Record rec;
            boolean found = false;
            for (StoreFile storeFile:storeFiles)
            {
                rec = storeFile.getRecord(key);
                if (rec != null)
                {
                    Record temp = new Record(rec);
                    temp.modify(value);
                    memStore.addRecord(temp);
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                memStore.addRecord(new Record(key, value));
            }
        }
        else
        {
            memStore.modify(key, value);
        }

        if (memStore.needDump())
        {
            dump();
        }
    }

    public void dump()
    {
        for (Record rec:memStore.getRecords())
        {
            boolean found = false;
            for (StoreFile storeFile:storeFiles)
            {
                if (storeFile.trySave(rec))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                StoreFile lastStoreFile = storeFiles.get(storeFiles.size() - 1);
                if (!lastStoreFile.full())
                {
                    lastStoreFile.add(rec);
                }
                else
                {
                    StoreFile newStoreFile = new StoreFile();
                    newStoreFile.add(rec);
                    this.storeFiles.add(newStoreFile);
                }
            }
        }
        memStore.clearRecords();
    }
}
