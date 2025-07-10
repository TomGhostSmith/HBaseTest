package cn.edu.fudan.tom;

import cn.edu.fudan.tom.entity.HBase;
import cn.edu.fudan.tom.entity.Value;
import cn.edu.fudan.tom.operation.Get;
import cn.edu.fudan.tom.operation.Put;
import cn.edu.fudan.tom.operation.Result;

import java.util.ArrayList;
import java.util.List;

public class HBaseApplication
{
    public static void main(String[] args)
    {
        // naive test
        HBase hBase = new HBase();
        hBase.addTable("table1");
        String[] columns = {"column1", "column2"};
        Result result = hBase.search("table1", new Get("key1", columns));
        if (result != null)
        {
            System.out.println("result should be null");
            return;
        }
        List<Value> values = new ArrayList<>();
        values.add(new Value("column1", 1));
        values.add(new Value("column2", "ValueForColumn2"));
        hBase.modify("table1", new Put("key1", values));
        result = hBase.search("table1", new Get("key1", columns));
        if (result == null)
        {
            System.out.println("result should not be null");
            return;
        }
        System.out.println("Naive test passed");

    }
}
