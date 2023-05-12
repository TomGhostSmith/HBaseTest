package cn.edu.fudan.tom.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Record
{
    Key key;
    List<Value> values;

    public Record(Key key, Value value)
    {
        this.key = key;
        this.values = new ArrayList<>();
        this.values.add(value);
    }

    public Record(Record rec)
    {
        this.key = new Key(rec.key);
        this.values = new ArrayList<>();
        for (Value value:rec.values)
        {
            this.values.add(new Value(value));
        }
    }

    public Value search(String columnName)
    {
        Value result = null;
        for (Value value:values)
        {
            if (value.getColumnName().equals(columnName))
            {
                result = value;
            }
        }
        return result;
    }

    public void modify(Value value)
    {
        boolean found = false;
        for (int i = 0;i < values.size();i++)
        {
            if (values.get(i).getColumnName().equals(value.getColumnName()))
            {
                values.set(i, value);
                found = true;
                break;
            }
        }
        if (!found)
        {
            values.add(value);
        }
    }
}
