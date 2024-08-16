package org.example.sync;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ClassName:AtomicReferenceTest
 * Package:org.example.cas
 * Description: java 原子引用类测试
 * AtomicReference 保证对象的原子性，但是不能保证对象中的属性的原子性
 *
 * @Date:2024/8/14 11:00
 * @Author:qs@1.com
 */
public class AtomicReferenceTest {
    public static void main(String[] args) {
        // 创建一个对象
        User user1 = new User("张三", 22);
        User user2 = new User("李四", 25);
        User user3 = new User("王五", 28);

        // 创建一个原子引用类
        AtomicReference<User> atomicReference = new AtomicReference<>(user1);

        boolean success = atomicReference.compareAndSet(user1, user2);
        System.out.println("CAS 操作：" + success + "，reference 值：" + atomicReference.get());

        success = atomicReference.compareAndSet(user1, user3);
        System.out.println("CAS 操作：" + success + "，reference 值：" + atomicReference.get());

        // function test
        User updateFunctionResult = atomicReference.getAndUpdate(user -> {
            user.setAge(user.getAge() + 1);
            return user;
        });
        System.out.println("updateFunctionResult: " + updateFunctionResult + "，reference 值：" + atomicReference.get());

        User accumulateFunctionResult = atomicReference.accumulateAndGet(new User("赵六", 30), (prev, x) -> {
            prev.setAge(prev.getAge() + x.getAge());
            return prev;
        });
        System.out.println("accumulateFunctionResult: " + accumulateFunctionResult + "，reference 值：" + atomicReference.get());
    }

}


@Data
@AllArgsConstructor
class User {
    private String name;
    private Integer age;
}
