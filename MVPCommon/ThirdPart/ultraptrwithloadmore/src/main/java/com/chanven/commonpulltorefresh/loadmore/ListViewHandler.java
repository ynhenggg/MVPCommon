package com.chanven.commonpulltorefresh.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.chanven.commonpulltorefresh.loadmore.ILoadViewMoreFactory.FootViewAdder;
import com.chanven.commonpulltorefresh.loadmore.ILoadViewMoreFactory.ILoadMoreView;
import com.chanven.commonpulltorefreshview.R;

public class ListViewHandler implements ViewHandler {
//	private static View mFooterView = null;

	@Override
	public boolean handleSetAdapter(View contentView, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
		final ListView listView = (ListView) contentView;
		boolean hasInit = false;
		if (loadMoreView != null) {
			final Context context = listView.getContext().getApplicationContext();
			loadMoreView.init(new FootViewAdder() {

				@Override
				public View addFootView(int layoutId) {
					View view = LayoutInflater.from(context).inflate(layoutId, listView, false);
//					mFooterView = view.findViewById(R.id.footer_root);
					return addFootView(view);
				}

				@Override
				public View addFootView(View view) {
					listView.addFooterView(view);
					return view;
				}
			}, onClickLoadMoreListener);
			hasInit = true;
		}
		return hasInit;
	}
	
	@Override
	public void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener) {
		ListView listView = (ListView) contentView;
		listView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
		listView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
	}

	/**
	 * 针对于电视 选择到了底部项的时候自动加载更多数据
	 */
	private class ListViewOnItemSelectedListener implements OnItemSelectedListener {
		private OnScrollBottomListener onScrollBottomListener;

		public ListViewOnItemSelectedListener(OnScrollBottomListener onScrollBottomListener) {
			super();
			this.onScrollBottomListener = onScrollBottomListener;
		}

		@Override
		public void onItemSelected(AdapterView<?> listView, View view, int position, long id) {
			if (listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScorllBootom();
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/**
	 * 滚动到底部自动加载更多数据
	 */
	private static class ListViewOnScrollListener implements OnScrollListener {
		private OnScrollBottomListener onScrollBottomListener;

		public ListViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
			super();
			this.onScrollBottomListener = onScrollBottomListener;
		}

		@Override
		public void onScrollStateChanged(AbsListView listView, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScorllBootom();
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//			if(totalItemCount > visibleItemCount){
//				if(mFooterView != null) {
//					mFooterView.setVisibility(View.VISIBLE);
//				}
//			}
		}
	};
}