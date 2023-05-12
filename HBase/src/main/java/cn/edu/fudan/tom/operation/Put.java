package cn.edu.fudan.tom.operation;

import cn.edu.fudan.tom.entity.Key;
import cn.edu.fudan.tom.entity.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Put
{
    Key key;
    List<Value> values;

    public Put(String key, List<Value> values)
    {
        this.key = new Key(key);
        this.values = values;
    }
}
