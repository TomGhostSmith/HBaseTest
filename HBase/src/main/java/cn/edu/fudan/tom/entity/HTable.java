package cn.edu.fudan.tom.entity;

import cn.edu.fudan.tom.config.Config;
import cn.edu.fudan.tom.operation.Get;
import cn.edu.fudan.tom.operation.Put;
import cn.edu.fudan.tom.operation.Result;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HTable
{
    String name;
    List<HRegion> regions;

    public HTable(String name)
    {
        this.name = name;
        this.regions = new ArrayList<>();
    }

    public Result search(Get get)
    {
        Result result = null;
        for (HRegion region:regions)
        {
            if (region.contain(get.getKey()))
            {
                result = region.search(get);
            }
        }
        return result;
    }

    public void modify(Put put)
    {
        boolean found = false;
        for (HRegion region:regions)
        {
            if (region.contain(put.getKey()))
            {
                region.modify(put);
                found = true;
                break;
            }
        }
        if (!found)
        {
            int keyHash = put.getKey().hashCode();
            int min = keyHash & Config.HRegionMask;
            int max = min + Config.HRegionSize;
            HRegion region = new HRegion(min, max);
            this.regions.add(region);
            region.modify(put);
        }
    }
}
