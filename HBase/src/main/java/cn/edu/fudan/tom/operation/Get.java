package cn.edu.fudan.tom.operation;

import cn.edu.fudan.tom.entity.Key;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Get
{
    Key key;
    List<String> columns;

    public Get(String key, String[] columns)
    {
        this.key = new Key(key);
        this.columns = Arrays.asList(columns);
    }
}
