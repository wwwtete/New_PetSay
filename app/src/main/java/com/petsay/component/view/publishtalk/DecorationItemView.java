package com.petsay.component.view.publishtalk;

import com.petsay.vo.decoration.DecorationBean;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/2
 * @Description
 */
public interface DecorationItemView {

    public void updateView(DecorationBean bean);
    public DecorationBean getData();
    public void showLoading(boolean flag);
    public void setDownloadFaile();
    public void setDownloadSuccess();
    public void resetView();

}
