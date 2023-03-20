package algonquin.cst2335.li000793;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class MessageDetailsFragment extends Fragment {
}
@Override

public View onCreateView(LayoutInflater, inflater, ViewGroup container, Bundle savedInstanceState){
super.onCreateView(inflater, container,savedInstanceState);

DetailsLayoutBinding binding= DetailsLayoutBinding.inflate(inflater);
return bingding.getRoot();

}