package cn.edu.fudan.tom.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemStore
{
    List<Record> records;

    public MemStore()
    {
        this.records = new ArrayList<>();
    }

    public Value search(Key key, String columnName)
    {
        Value result = null;
        for (Record rec:records)
        {
            if (rec.getKey().equals(key))
            {
                result = rec.search(columnName);
                break;
            }
        }
        return result;
    }

    public void modify(Key key, Value value)
    {
        for (Record rec:records)
        {
            if (rec.getKey().equals(key))
            {
                rec.modify(value);
                break;
            }
        }
    }

    public boolean existsKey(Key key)
    {
        boolean result = false;
        for (Record rec:records)
        {
            if (rec.getKey().equals(key))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public void addRecord(Record rec)
    {
        records.add(rec);
    }

    public void clearRecords()
    {
        records = new ArrayList<>();
    }

    public boolean needDump()
    {
        return records.size() > 10;
    }
}
