package kiway.cn.webviewjszhuru.photoviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kiway.cn.webviewjszhuru.R;
import kiway.cn.webviewjszhuru.photoviews.views.MyPhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 图像浏览帧
 *
 * @author YI
 */
public class ViewPhotoFragment extends Fragment implements
        OnPhotoTapListener {
    /**
     * 图像地址
     */
    String url;
    MyPhotoView myPhotoView;

    View view;
    public static ViewPhotoFragment newInstance(String url) {
        ViewPhotoFragment newFragment = new ViewPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.url = args.getString("url");
        }
    }

    public ViewPhotoFragment() {
        super();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.yjpty_fragment_view_photo, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myPhotoView = (MyPhotoView) view.findViewById( R.id.img);
        myPhotoView.setPhotoUri(url);
    }


    @Override
    public void onPhotoTap(View view, float v, float v1) {
    getActivity().finish();
    }
}
