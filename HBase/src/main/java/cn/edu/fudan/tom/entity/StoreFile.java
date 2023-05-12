package cn.edu.fudan.tom.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreFile
{
    List<Record> records;

    public Value search(Key key, String columnName)
    {
        Value result = null;
        for (Record rec:records)
        {
            if (rec.getKey().equals(key))
            {
                result = rec.search(columnName);
            }
        }
        return result;
    }

    public Record getRecord(Key key)
    {
        Record result = null;
        for (Record rec:records)
        {
            if (rec.getKey().equals(key))
            {
                result = rec;
            }
        }
        return result;
    }

    public boolean trySave(Record rec)
    {
        boolean result = false;
        for (int i = 0;i < records.size();i++)
        {
            if (records.get(i).getKey().equals(rec.getKey()))
            {
                records.set(i, rec);
                result = true;
                break;
            }
        }
        return result;
    }

    public void add(Record rec)
    {
        this.records.add(rec);
    }

    public boolean full()
    {
        return this.records.size() >= 10;
    }
}
