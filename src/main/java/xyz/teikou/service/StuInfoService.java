package xyz.teikou.service;

import xyz.teikou.entity.StuInfo;

import java.util.List;

/**
 * Creat by TeiKou
 * 2019/10/11 14:12
 */
public interface StuInfoService {
    public void addInfo(StuInfo stuInfo);

    public StuInfo myInfo(Integer id);

    public void  updateInfo(StuInfo stuInfo);

    public List<StuInfo> allInfo();

    public StuInfo theInfo(String schNumber);
}
