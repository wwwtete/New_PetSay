package com.petsay.vo.petalk;

/**
 *
 * 相册列表当个item
 *
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/1
 * @Description
 */
public class PhotoAlbumItem {

    public String folderPath;
    public int fileCount;
    public String thumbnailPath;


    public PhotoAlbumItem(String folderPath,int count,String firstImgPath) {
        super();
        this.folderPath = folderPath;
        this.fileCount = count;
        this.thumbnailPath = firstImgPath;
    }

    /**
     * Constructs a new instance of {@code Object}.
     */
    public PhotoAlbumItem() {
        super();
    }
}
