package cn.edu.fudan.tom.entity;

import cn.edu.fudan.tom.operation.Get;
import cn.edu.fudan.tom.operation.Put;
import cn.edu.fudan.tom.operation.Result;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HBase
{
    List<HTable> tables;

    public HBase()
    {
        this.tables = new ArrayList<>();
    }

    public Result search(String tableName, Get get)
    {
        Result result = null;
        for (HTable table:tables)
        {
            if (table.getName().equals(tableName))
            {
                result = table.search(get);
                break;
            }
        }
        return result;
    }

    public void modify(String tableName, Put put)
    {
        for (HTable table:tables)
        {
            if (table.getName().equals(tableName))
            {
                table.modify(put);
                break;
            }
        }
    }

    public void addTable(String tableName)
    {
        this.tables.add(new HTable(tableName));
    }

}
