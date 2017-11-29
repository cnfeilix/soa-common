package top.feilix.soa.lock.db;

import top.feilix.soa.lock.IDistributedLock;

public class ResourceAccessLock implements IDistributedLock {
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(ResourceAccessLock.class);

	@Override
	public boolean lock(String lockId) {
		return false;
	}

	@Override
	public boolean unlock(String lockId) {
		return false;
	}

	@Override
	public boolean occupy(String resource, int deakLockTimeOut) {
		ResourceAccess ra = selectOneByResource(resource);
		// 未建立资源，不允许占用
		if (ra == null) {
			return false;
		}
		// 死锁超时检查
		boolean deakLock = false;
		if (ra.getUpdateTime() == null
				|| ((System.currentTimeMillis() - ra.getUpdateTime().getTime()) / 1000 > deakLockTimeOut)) {
			deakLock = true;
			ra.setState(0);
			// 未成功释放锁，已被竞争，退出
			if (!tryUpdate(ra)) {
				return false;
			}
		}
		// 继续或者重新竞争锁
		if (deakLock) {
			ra = this.selectOneByResource(resource);
		}
		if (ra.getState() == 1) {
			return false;
		}
		ra.setState(1);
		return tryUpdate(ra);
	}

	@Override
	public void release(String resource) {
		ResourceAccess ra = new ResourceAccess();
		ra.setState(0);
		ResourceAccessExample example = new ResourceAccessExample();
		Criteria criteria = example.createCriteria();
		criteria.andResourceEqualTo(resource);
		this.updateByExampleSelective(ra, example);
	}

	private boolean tryUpdate(ResourceAccess ra) {
		long oldVersion = ra.getVersion();
		ra.setVersion(oldVersion + 1);
		// ra.setUpdateTime(new Date());//交给数据库自动更新，使用数据库时间
		ResourceAccessExample example = new ResourceAccessExample();
		Criteria criteria = example.createCriteria();
		criteria.andResourceEqualTo(ra.getResource());
		criteria.andVersionEqualTo(oldVersion);

		return this.updateByExampleSelective(ra, example) > 0;
	}

	private ResourceAccess selectOneByResource(String resource) {
		ResourceAccessExample example = new ResourceAccessExample();
		Criteria criteria = example.createCriteria();
		criteria.andResourceEqualTo(resource);
		return this.selectOneByExample(example);
	}
}
