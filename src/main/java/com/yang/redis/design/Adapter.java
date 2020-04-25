package com.yang.redis.design;

/**
 * @author yangyuyang
 * @date 2020/3/23 19:55
 */
public class Adapter {
    public static void main(String[] args) {
        Phone phone = new Phone();
        VoltageAdapter adapter = new VoltageAdapter();
        phone.setVoltageAdapter(adapter);
        phone.charge();
    }

}

class Phone {
     static final Integer v = 200;

     VoltageAdapter voltageAdapter;

     public void charge() {
         voltageAdapter.changeVoltage();
     }

     public void setVoltageAdapter(VoltageAdapter adapter) {
         this.voltageAdapter = adapter;
     }

}

class VoltageAdapter {
    public void changeVoltage() {
        System.out.println(Phone.v - 200);
    }
}
