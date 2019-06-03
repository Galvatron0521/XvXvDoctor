package cn.com.xxdoctor.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by KyuYi on 2017/3/2.
 * E-Mail:kyu_yi@sina.com
 * 功能：
 */

public class CardBean implements IPickerViewData {
    public String cardItemId;
    public String cardItemName;
    public Object tag;
    //课题使用
    public String id;
    public String isHaveChild;
    public String fieldName;

    public CardBean(String cardItemId, String cardItemName) {
        this.cardItemId = cardItemId;
        this.cardItemName = cardItemName;
    }

    public CardBean(String cardItemId, String cardItemName, String id, String isHaveChild,String fieldName) {
        this.cardItemId = cardItemId;
        this.cardItemName = cardItemName;
        this.id = id;
        this.isHaveChild = isHaveChild;
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return "CardBean{" +
                "cardItemId='" + cardItemId + '\'' +
                ", cardItemName='" + cardItemName + '\'' +
                ", tag=" + tag +
                ", id='" + id + '\'' +
                ", isHaveChild='" + isHaveChild + '\'' +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }

    public String getId() {
        return cardItemId;
    }

    public void setId(String id) {
        this.cardItemId = id;
    }

    public String getCardNo() {
        return cardItemName;
    }

    public void setCardNo(String cardNo) {
        this.cardItemName = cardNo;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String getPickerViewText() {
        return cardItemName;
    }

}

