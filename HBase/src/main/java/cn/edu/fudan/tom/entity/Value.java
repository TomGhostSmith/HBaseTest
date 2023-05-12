package cn.edu.fudan.tom.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Value
{
    String columnName;
    Object val;

    public Value(Value val)
    {
        this.columnName = val.columnName;
        this.val = val.val;
    }

    public Value(String columnName, Object val)
    {
        this.columnName = columnName;
        this.val = val;
    }
}
