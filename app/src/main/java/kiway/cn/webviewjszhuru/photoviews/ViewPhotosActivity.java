package kiway.cn.webviewjszhuru.photoviews;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import org.json.JSONArray;

import kiway.cn.webviewjszhuru.R;
import kiway.cn.webviewjszhuru.photoviews.views.HackyViewPager;


/**
 * 浏览多页图片
 * 
 * @author Zao 可传两种类型参数 字符串类型和自定义类型的集合 参数名称为BUNDLE_PARAMS(图像集合)
 *         BUNDLE_PARAMS1(第几个)
 * */
public class ViewPhotosActivity extends FragmentActivity implements
		OnPageChangeListener {
	HackyViewPager pager;
	JSONArray urls;
	int page = 0;
	ViewPhotoPagerAdapter adapter;
	View view;
	TextView step;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		view =  LayoutInflater.from(this).inflate(R.layout.yjpty_activity_view_photos, null);
		setContentView(view,  new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		pager = (HackyViewPager) view.findViewById(R.id.vPager);
		step= (TextView) view.findViewById(R.id.step);
		try {
			initView();
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据不同的值初始化适配器
	 * */
	@SuppressWarnings("unchecked")
	public void initView() throws Exception {
		urls = new JSONArray(getIntent().getExtras()
				.getString("urls"));
		adapter = new ViewPhotoPagerAdapter(getSupportFragmentManager(), urls);
		page = getIntent().getExtras().getInt("position");
		step.setText((page+1)+"/"+urls.length());
	}

	public void loadData() throws Exception {
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		if (page < adapter.getCount())
			pager.setCurrentItem(page);
		if (adapter.getCount() > 1)
			findViewById(R.id.step).setVisibility(View.VISIBLE);
		else
			findViewById(R.id.step).setVisibility(View.GONE);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int page) {
		step.setText((page+1)+"/"+urls.length());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
