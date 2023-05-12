package cn.edu.fudan.tom.operation;

import cn.edu.fudan.tom.entity.Key;
import cn.edu.fudan.tom.entity.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Result
{
    Key key;
    List<Value> values;

    public Result(Key key)
    {
        this.key = key;
        this.values = new ArrayList<>();
    }

    public void addValue(Value value)
    {
        this.values.add(value);
    }
}
