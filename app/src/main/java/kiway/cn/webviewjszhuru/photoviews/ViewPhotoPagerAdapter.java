package kiway.cn.webviewjszhuru.photoviews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 图片浏览分页适配器
 * 
 * @author YI
 * */
public class ViewPhotoPagerAdapter extends FragmentPagerAdapter {
	JSONArray urls;

	public ViewPhotoPagerAdapter(FragmentManager fm,JSONArray urls) {
		super(fm);
		this.urls = urls;
	}

	@Override
	public Fragment getItem(int position) {
		try {
			return  ViewPhotoFragment.newInstance(urls.getString(position));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ViewPhotoFragment.newInstance("");
	}

	@Override
	public int getCount() {
			return urls.length();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
