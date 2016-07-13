package net.shopxx.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.plugin.StoragePlugin;
import net.shopxx.service.PluginService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;

@Service("pluginServiceImpl")
public class PluginServiceImpl implements PluginService {
	@Resource
	private List<PaymentPlugin> IIIllIlI = new ArrayList();
	@Resource
	private List<StoragePlugin> IIIllIll = new ArrayList();
	@Resource
	private Map<String, PaymentPlugin> IIIlllII = new HashMap();
	@Resource
	private Map<String, StoragePlugin> IIIlllIl = new HashMap();

	public List<PaymentPlugin> getPaymentPlugins() {
		Collections.sort(this.IIIllIlI);
		return this.IIIllIlI;
	}

	public List<StoragePlugin> getStoragePlugins() {
		Collections.sort(this.IIIllIll);
		return this.IIIllIll;
	}

	public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled)
  {
    ArrayList localArrayList = new ArrayList();
    CollectionUtils.select(this.IIIllIlI, new _cls1(isEnabled), localArrayList);
    Collections.sort(localArrayList);
    return localArrayList;
  }

	public List<StoragePlugin> getStoragePlugins(boolean isEnabled)
  {
    ArrayList localArrayList = new ArrayList();
    CollectionUtils.select(this.IIIllIll, new _cls2(isEnabled), localArrayList);
    Collections.sort(localArrayList);
    return localArrayList;
  }

	public PaymentPlugin getPaymentPlugin(String id) {
		return (PaymentPlugin) this.IIIlllII.get(id);
	}

	public StoragePlugin getStoragePlugin(String id) {
		return (StoragePlugin) this.IIIlllIl.get(id);
	}

	private class _cls1 implements Predicate {

		_cls1(boolean flag) {
			super();
			IIIllIlI = PluginServiceImpl.this;
			IIIllIll = flag;

		}

		public boolean evaluate(Object object) {
			PaymentPlugin paymentplugin = (PaymentPlugin) object;
			return paymentplugin.getIsEnabled() == IIIllIll;
		}

		final PluginServiceImpl IIIllIlI;
		private final boolean IIIllIll;

	}

	private class _cls2 implements Predicate {

		public boolean evaluate(Object object) {
			StoragePlugin storageplugin = (StoragePlugin) object;
			return storageplugin.getIsEnabled() == IIIllIll;
		}

		final PluginServiceImpl IIIllIlI;
		private final boolean IIIllIll;

		_cls2(boolean flag) {
			super();
			IIIllIlI = PluginServiceImpl.this;
			IIIllIll = flag;

		}
	}

}