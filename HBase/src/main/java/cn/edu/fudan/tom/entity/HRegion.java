package cn.edu.fudan.tom.entity;

import cn.edu.fudan.tom.config.Config;
import cn.edu.fudan.tom.operation.Get;
import cn.edu.fudan.tom.operation.Put;
import cn.edu.fudan.tom.operation.Result;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HRegion
{
    List<Store> stores;
    private int minKeyHash;
    private int maxKeyHash;

    public HRegion(int minKeyHash, int maxKeyHash)
    {
        this.minKeyHash = minKeyHash;
        this.maxKeyHash = maxKeyHash;
        this.stores = new ArrayList<>();
    }

    public boolean contain(Key key)
    {
        int hashCode = key.hashCode();
        return hashCode >= minKeyHash && hashCode < maxKeyHash;
    }

    public Result search(Get get)
    {
        Result result = new Result(get.getKey());
        for (String column:get.getColumns())
        {
            for (Store store:stores)
            {
                if (store.contain(column))
                {
                    result.addValue(store.search(get.getKey(), column));
                    break;
                }
            }
        }
        return result;
    }

    public void modify(Put put)
    {
        Key key = put.getKey();
        for (Value value: put.getValues())
        {
            boolean found = false;
            String columnName = value.getColumnName();
            for (Store store:stores)
            {
                if (store.contain(columnName))
                {
                    store.modify(key, value);
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                int columnNameHash = value.getColumnName().hashCode();
                int minHash = columnNameHash & Config.StoreMask;
                int maxHash = minHash + Config.StoreSize;
                Store store = new Store(minHash, maxHash);
                this.stores.add(store);
                store.modify(key, value);
            }
        }
    }
}
