package com.amp.apis.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.clustermarkerlibrary.R;

/**
 * @author yiyi.qi 用于展示聚合的overlay
 */
public class ClusterOverlay implements OnCameraChangeListener,
		OnMarkerClickListener {
	private List<ClusterItem> mPoints;
	private AMap mAMap;
	private List<Cluster> mClusters;
	private int mClusterSize;
	private Projection mProjection;
	private Context mContext;
	private ExecutorService executor;
	private float level = 0;
	private ClusterClickListener mClusterClickListener;
	private ClusterRender mClusterRender;

	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			addClusterToMap();
		}
	};

	/**
	 * 构造函数
	 * 
	 * @param amap
	 * @param clusterSize
	 *            聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
	 * @param context
	 */
	public ClusterOverlay(AMap amap, int clusterSize, Context context) {
		this(amap, null, clusterSize, context);

	}

	/**
	 * 构造函数
	 * 
	 * @param amap
	 * @param clusterItems
	 *            聚合元素
	 * @param clusterSize
	 * @param context
	 */
	public ClusterOverlay(AMap amap, List<ClusterItem> clusterItems,
			int clusterSize, Context context) {
		if (clusterItems != null) {
			mPoints = clusterItems;
		} else {
			mPoints = new ArrayList<ClusterItem>();
		}
		mContext = context;
		mClusters = new ArrayList<Cluster>();
		this.mAMap = amap;

		amap.setOnCameraChangeListener(this);
		amap.setOnMarkerClickListener(this);
		mProjection = amap.getProjection();
		mClusterSize = clusterSize;
		executor = Executors.newFixedThreadPool(2);
		assignClusters();
		 
	}

	/**
	 * 设置聚合点的点击事件
	 * 
	 * @param clusterClickListener
	 */
	public void setOnClusterClickListener(
			ClusterClickListener clusterClickListener) {
		mClusterClickListener = clusterClickListener;
	}

	/**
	 * 添加一个聚合点
	 * 
	 * @param item
	 */
	public void addClusterItem(ClusterItem item) {
		mPoints.add(item);
		assignSingleCluster(item);

	}

	/**
	 * 设置聚合元素的渲染样式，不设置则默认为气泡加数字形式进行渲染
	 * @param render
	 */
	public void setClusterRenderer(ClusterRender render) {
		mClusterRender = render;
	}
/**
 * 将聚合元素添加至地图上
 * */
	private void addClusterToMap() {
		mAMap.clear();
		for (Cluster cluster : mClusters) {
			addSingleClusterToMap(cluster);
		}

	}

	/**
	 * 将单个聚合元素添加至地图显示
	 * @param cluster
	 */
	private void addSingleClusterToMap(Cluster cluster) {
		LatLng latlng = cluster.getCenterLatLng();
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.anchor(0.5f, 0.5f)
				.icon(getBitmapDes(cluster.getClusterCount())).position(latlng);
		Marker marker = mAMap.addMarker(markerOptions);
		cluster.setMarker(marker);
	}
/**
 * 获取每个聚合点的绘制样式
 * */
	private BitmapDescriptor getBitmapDes(int num) {
		TextView textView = new TextView(mContext);
		if (num > 1) {
			String tile = String.valueOf(num);
			textView.setText(tile);
		}
		textView.setGravity(Gravity.CENTER);

		textView.setTextColor(Color.BLACK);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		if (mClusterRender != null && mClusterRender.getDrawAble(num) != null) {
			textView.setBackgroundDrawable(mClusterRender.getDrawAble(num));
		} else {
			textView.setBackgroundResource(R.drawable.defaultcluster);
		}
		return BitmapDescriptorFactory.fromView(textView);
	}
/**
 * 更新已加入地图聚合点的样式
 * */
	private void updateCluster(Cluster cluster) {
		Marker marker = cluster.getMarker();
		marker.setIcon(getBitmapDes(cluster.getClusterCount()));
	}

	/**
	 * 对点进行聚合
	 */
	private void assignClusters() {
		mClusters.clear();
		executor.submit(new Runnable() {

			@Override
			public void run() {
				for (ClusterItem clusterItem : mPoints) {
					LatLng latlng = clusterItem.getPosition();
					Point point = mProjection.toScreenLocation(latlng);
					Cluster cluster = getCluster(point);
					if (cluster != null) {
						cluster.addClusterItem(clusterItem);
					} else {
						cluster = new Cluster(point, latlng);
						mClusters.add(cluster);
						cluster.addClusterItem(clusterItem);
					}
				}
				handler.sendEmptyMessage(0);
			}
		});

	}

	/**
	 * 在已有的聚合基础上，对添加的单个元素进行聚合
	 * @param clusterItem
	 */
	private void assignSingleCluster(ClusterItem clusterItem) {
		LatLng latlng = clusterItem.getPosition();
		Point point = mProjection.toScreenLocation(latlng);
		Cluster cluster = getCluster(point);
		if (cluster != null) {
			cluster.addClusterItem(clusterItem);
			updateCluster(cluster);
		} else {
			cluster = new Cluster(point, latlng);
			mClusters.add(cluster);
			cluster.addClusterItem(clusterItem);
			addSingleClusterToMap(cluster);
		}
	}

	/**
	 * 根据一个点获取是否可以依附的聚合点，没有则返回null
	 * @param point
	 * @return
	 */
	private Cluster getCluster(Point point) {
		for (Cluster cluster : mClusters) {
			Point poi = cluster.getCenterPoint();
			double distance = getDistanceBetweenTwoPoints(point.x, point.y,
					poi.x, poi.y);
			if (distance < mClusterSize) {
				return cluster;
			}
		}

		return null;
	}

	/**
	 * 两点的距离
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private double getDistanceBetweenTwoPoints(double x1, double y1, double x2,
			double y2) {
		double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
				* (y1 - y2));
		return distance;
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub

	}
//放大缩小完成后对聚合点进行重新计算
	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {
		float leveltemp = mAMap.getCameraPosition().zoom;
		if (leveltemp != level) {
			assignClusters();
			level = leveltemp;
		}

	}
//点击事件
	@Override
	public boolean onMarkerClick(Marker arg0) {
		if (mClusterClickListener == null) {
			return false;
		}
		for (Cluster cluster : mClusters) {
			if (arg0.equals(cluster.getMarker())) {
				mClusterClickListener.onClick(arg0, cluster.getClusterItems());
				return false;
			}
		}
		return false;
	}
}
