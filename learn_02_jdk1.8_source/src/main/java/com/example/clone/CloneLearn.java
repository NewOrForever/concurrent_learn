package com.example.clone;

/**
 * ClassName:demo
 * Package:com.example
 * Description:
 *
 * @Date:2024/2/7 15:35
 * @Author:qs@1.com
 */
public class CloneLearn {
    public static void main(String[] args) throws CloneNotSupportedException {
        Person person = new Person();
        person.setName("张三");
        person.setAge(18);
        Address address = new Address();
        address.setProvince("广东省");
        address.setCity("深圳市");
        address.setStreet("南山区");
        person.setAddress(address);

        Person clonePerson = (Person)person.clone();
        System.out.println(person == clonePerson);

        /**
         * 修改 clonePerson 的属性，测试 person 是否会受到影响
         * 1. 修改 clonePerson 的基本数据类型属性，person 不会受到影响
         * 2. 修改 clonePerson 的字符串类型属性，person 不会受到影响
         * 3. 修改 clonePerson 的引用类型属性，person 会受到影响
          */
        clonePerson.setName("李四");
        clonePerson.setAge(20);
        Address cloneAddress = clonePerson.getAddress();
        cloneAddress.setProvince("广西省");

        System.out.println("测试结束");
    }
}

class Person implements Cloneable{
    private String name;
    /**
     * Integer 是包装类，是引用类型
     * clone 后的对象和原对象是独立的，修改一个对象不会影响另一个对象
      */
    private int age;
    private Address address;



    /**
     * 写 clone  方法的时候需要注意：
     * 1. 重写 clone 方法，返回类型需要是当前类的类型
     * 2. 重写 clone 方法，需要实现 Cloneable 接口
     *
     * 浅拷贝 - 仅仅拷贝对象本身，不拷贝对象内部的引用
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person clonePerson = (Person) super.clone();
        return clonePerson;
    }


    /**
     * 深拷贝 - 拷贝对象本身，同时拷贝对象内部的引用
     * 1. 深拷贝就是在拷贝对象本身的同时，也拷贝对象内部的引用直至基本数据类型、String 类型
     * 2. 深拷贝后返回的对象和原对象是完全独立的，修改一个对象不会影响另一个对象
     * @return
     * @throws CloneNotSupportedException
     */
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        Person clonePerson = (Person) super.clone();
//
//        Address cloneAddress = (Address) (this.getAddress().clone());
//        clonePerson.setAddress(cloneAddress);
//        return clonePerson;
//    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
class Address implements Cloneable{
    private String province;
    private String city;
    private String street;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
