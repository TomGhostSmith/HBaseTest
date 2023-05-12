package cn.edu.fudan.tom.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Key
{
    String keyIndex;

    public Key(String key)
    {
        this.keyIndex = key;
    }

    public Key(Key key)
    {
        this.keyIndex = key.keyIndex;
    }

    @Override
    public boolean equals(Object o)
    {
        boolean result;
        if (o instanceof Key)
        {
            Key key = (Key) o;
            result = key.keyIndex.equals(this.keyIndex);
        }
        else
        {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode()
    {
        return keyIndex.hashCode();
    }
}
