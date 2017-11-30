package test.function;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.pojo.User;
import top.feilix.soa.common.util.ReflectAsmUtil;
import top.feilix.soa.common.util.ReflectAsmUtil;
import top.feilix.soa.common.util.ReflectAsmUtil;

/**
 * 测试几种JavaBean属性复制工具的性能
 * 结果->
 * 效率：原生复制  >> reflect-asm > spring >> apache
 * 推荐：spring
 * @author feilix
 *
 */
public class TestPerformanceOfCopyProperties {
	
	public static void useApacheBeanUtils(List<User> srcs) throws Exception {
		List<User> dests = new ArrayList<User>();
		long start = System.currentTimeMillis();
		for(User src : srcs) {
			User dest = new User();
			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, src);
			dests.add(dest);
		}
		System.out.println("org.apache.commons.BeanUtils.copyProperties->" + srcs.size() + "个对象复制耗时：" + ((System.currentTimeMillis() - start)*1.0/1000) + "秒");	
	}
	
	public static List<User> useSpringBeanUtils(List<User> srcs) throws Exception {
		List<User> dests = new ArrayList<User>();
		long start = System.currentTimeMillis();
		for(User src : srcs) {
			User dest = new User();
			org.springframework.beans.BeanUtils.copyProperties(src, dest);
			dests.add(dest);
		}
		System.out.println("org.springframework.beans.BeanUtils.copyProperties->" + srcs.size() + "个对象复制耗时：" + ((System.currentTimeMillis() - start)*1.0/1000) + "秒");	
		return dests;
	}
	
	public static void useReflectAsmBeanUtils(List<User> srcs) throws Exception {
		List<User> dests = new ArrayList<User>();
		long start = System.currentTimeMillis();
		for(User src : srcs) {
			User dest = new User();
			ReflectAsmUtil.copyProperties(src, dest);
			dests.add(dest);
		}
		System.out.println("ReflectAsmUtil.copyProperties->" + srcs.size() + "个对象复制耗时：" + ((System.currentTimeMillis() - start)*1.0/1000) + "秒");	
	}
	
	public static void copyRaw(List<User> srcs) throws Exception {
		List<User> dests = new ArrayList<User>();
		long start = System.currentTimeMillis();
		for(User src : srcs) {
			User dest = new User();
			dest.setId(src.getId());
			dest.setName(src.getName());
			dest.setEmail(src.getEmail());
			dest.setBirthDay(src.getBirthDay());
			dest.setCashAmount(src.getCashAmount());
			dest.setAddress(src.getAddress());
			dest.setAge(src.getAge());
			dests.add(dest);
		}
		System.out.println("copyOdd->" + srcs.size() + "个对象复制耗时：" + ((System.currentTimeMillis() - start)*1.0/1000) + "秒");	
	}
	
	private static void createSrcList(List<User> srcs, int count) {
		Date birth = new Date();
		for(int i = 0; i < count; i++) {
			User src = new User();
			src.setId(0L);
			src.setName("feilix");
			src.setEmail("feilix.cai@gmail.com");
			src.setCashAmount(BigDecimal.ZERO);
			src.setBirthDay(birth);
			src.setAddress("上海市青浦区e通世界南区");
			src.setAge(33);
			srcs.add(src);
		}
	}
	
	public static void main(String[] args)  throws Exception {
		int count = 1000000;
		List<User> srcs = new ArrayList<User>();
		createSrcList(srcs, count);
		
		//TestReflectAsmUtil.useApacheBeanUtils(srcs);
		TestPerformanceOfCopyProperties.copyRaw(srcs);
		TestPerformanceOfCopyProperties.useSpringBeanUtils(srcs);
		TestPerformanceOfCopyProperties.useReflectAsmBeanUtils(srcs);
		
		//第二次调用会有内部缓存优化性能，效率可提升一倍
		System.out.println("--------------------------------------------------------");
		//TestReflectAsmUtil.useApacheBeanUtils(srcs);
		TestPerformanceOfCopyProperties.copyRaw(srcs);
		TestPerformanceOfCopyProperties.useSpringBeanUtils(srcs);
		TestPerformanceOfCopyProperties.useReflectAsmBeanUtils(srcs);
		
		System.out.println("--------------------------------------------------------");
		//TestReflectAsmUtil.useApacheBeanUtils(srcs);
		TestPerformanceOfCopyProperties.copyRaw(srcs);
		TestPerformanceOfCopyProperties.useSpringBeanUtils(srcs);
		TestPerformanceOfCopyProperties.useReflectAsmBeanUtils(srcs);
		
	}
}
