package de.zib.gndms.gndmstest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.jndi.toolkit.url.Uri;

import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.model.gorfx.types.TaskControl;
import de.zib.gndms.common.model.gorfx.types.TaskFailure;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.model.gorfx.types.TaskStatus;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.FullGORFXClient;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.model.gorfx.types.TaskState;

public abstract class AbstractStagingClient extends AbstractTaskFlowExecClient{
	
	ApplicationContext context;
	String gorfxEpUrl;
	public String id;
	
	public AbstractStagingClient(String url) throws Exception {
		this.gorfxEpUrl = url;
		setupAll();
	}

	protected void setupAll() throws Exception {
		context = new ClassPathXmlApplicationContext(
				"classpath:META-INF/spring/bundle-context.xml");
		FullGORFXClient fgx = createBean(FullGORFXClient.class);
		fgx.setServiceURL(gorfxEpUrl);
		setGorfxClient(fgx);
		
		TaskFlowClient tfc = createBean(TaskFlowClient.class);
		tfc.setServiceURL(gorfxEpUrl);
		setTfClient(tfc);
	
		TaskClient tc = createBean(TaskClient.class);
		tc.setServiceURL(gorfxEpUrl);
		setTaskClient(tc);
	}

	public <T> T createBean(final Class<T> beanClass) {
		return createBean(beanClass,
				AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
	}

	public <T> T createBean(final Class<T> beanClass, int autowireMode) {
		return (T) context.getAutowireCapableBeanFactory().createBean(
				beanClass, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
	}

	@SuppressWarnings("unchecked")
	public <T> T createClientObject(Class<T> clazz) {
		return (T) context.getAutowireCapableBeanFactory().createBean(clazz,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
	}
	
	public void handleStatus(TaskStatus stat) {
		System.out.println(String.format("status %s", stat.getStatus()));
	}

	@Override
	protected Integer selectQuote(List<Specifier<Quote>> quotes) {
		ArrayList<String> l = new ArrayList<String>();
		for (Specifier<Quote> s : quotes) {
			l.add(s.getPayload().getSite());
			System.out.println(String.format("specifier %s", s ));
			Quote q=s.getPayload();
			System.out.println(String.format("expectedSize %s", q.getExpectedSize()));
			System.out.println(String.format("s.getPayload().getSite() %s", s.getPayload().getSite()));
		}
		System.out.println(String.format("wähle quote %s", l));
		return 0;
	}

	@Override
	protected void handleTaskSpecifier(Specifier<Facets> ts) {
		System.out.println(String.format("handleTask %s", ts.getUrl()));
		String[] splitResult = ts.getUrl().split("_");
		System.out.println(splitResult[1]);
		id =splitResult[1];
		String taskID = ts.getUriMap().get(UriFactory.TASK_ID);
		System.out.println(taskID);

	}

	@Override
	public void handleResult(TaskResult res) {
		System.out.println(String.format("ergebnis %s", res.getResult()));
	}

	@Override
	public void handleFailure(TaskFailure fail) {
		System.out.println(String.format("fehler %s", fail.getFaultTrace()));
	}
	
	@Override
	protected abstract GNDMSResponseHeader setupContext(GNDMSResponseHeader context);

}
