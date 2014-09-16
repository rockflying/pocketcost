package com.hzm.lczs;

import android.os.Parcel;
import android.os.Parcelable;

public class BankInfo implements Parcelable 
{
    //{"ID","CARD_NUM","BANK_ID","CARD_TYPE","BILL_DATE","REPAYMENT_TYPE","REPAYMENT_DATE","CREDIT_LIMIT","REMARK"}
    //app id
    int Id=-1;
    String CardNum=null;
    int BankId=-1;
    int CardType=-1;
    int BillDate=-1;
    int RePayType=-1;
    int RePayDate=-1;
    double CreditLimit=-1;
    String Remark;
    int AlarmId=-1;
    
    
    public int getId(){
        return Id;
    }
    public void setId(int id){
        this.Id=id;
    }
    
    public String getCardNum(){
        return CardNum;
    }
    public void setCardNum(String Num){
        this.CardNum=Num;
    }
    
    public int getBankId(){
        return BankId;
    }
    public void setBankId(int BankId){
        this.BankId=BankId;
    }
    
    public int getAlarmId(){
        return AlarmId;
    }
    public void setAlarmId(int AlarmId){
        this.AlarmId=AlarmId;
    }
    
    
    
    public int getCardType(){
        return CardType;
    }
    public void setCardType(int CardType){
        this.CardType=CardType;
    }
    
    public int getBillDate(){
        return BillDate;
    }
    public void setBillDate(int BillDate){
        this.BillDate=BillDate;
    }
    
    public int getRePayType(){
        return RePayType;
    }
    public void setRePayType(int RePayType){
        this.RePayType=RePayType;
    }
    
    
    public int getRePayDate(){
        return RePayDate;
    }
    public void setRePayDate(int RePayDate){
        this.RePayDate=RePayDate;
    }
    
    
    public double getCreditLimit(){
        return CreditLimit;
    }
    public void setCreditLimit(double CreditLimit){
        this.CreditLimit=CreditLimit;
    }
    
    public String getAppName(){
        return Remark;
    }
    public void setAppName(String Remark){
        this.Remark=Remark;
    }
    
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
        dest.writeInt(Id);
        dest.writeString(CardNum);
        dest.writeInt(BankId);
        dest.writeInt(CardType);
        dest.writeInt(BillDate);
        dest.writeInt(RePayType);
        dest.writeInt(RePayDate);
        dest.writeDouble(CreditLimit);
        dest.writeString(Remark);
        dest.writeInt(AlarmId);
        
    }
    
    public static final Parcelable.Creator<BankInfo> CREATOR = new Parcelable.Creator<BankInfo>() 
            {
                public BankInfo createFromParcel(Parcel in) {
                    return new BankInfo(in);
                }
            
                public BankInfo[] newArray(int size) {
                    return new BankInfo[size];
                }
            };

            private BankInfo(Parcel in) {
                Id          = in.readInt();
                CardNum     = in.readString();
                BankId      = in.readInt();            
                CardType    = in.readInt();
                BillDate    = in.readInt();
                RePayType   = in.readInt();
                RePayDate   = in.readInt();
                CreditLimit = in.readDouble();
                Remark      = in.readString();
                AlarmId     = in.readInt();
            }
            
            public BankInfo(int Id,String CardNum, int BankId, int CardType, int BillDate, int RePayType, int RePayDate, double CreditLimit, String Remark,int alarmId)
            {
                this.Id = Id;
                this.CardNum = CardNum;
                this.BankId = BankId;
                this.CardType = CardType;
                this.BillDate = BillDate;
                this.RePayType = RePayType;
                this.RePayDate = RePayDate;
                this.CreditLimit = CreditLimit;
                this.Remark = Remark;
                this.AlarmId = alarmId;
            }

}
