package com.hengda.zwf.commonadapter;

public interface RSectionSupport<T> {

    int sectionLayoutId();

    void sectionConvert(ViewHolder holder, T t);

    String getTitle(T t);

}
