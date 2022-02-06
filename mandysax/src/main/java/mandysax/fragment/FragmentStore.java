package mandysax.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

class FragmentStore {

    private final HashMap<String, Store> mStore = new HashMap<>();
    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    private Store findStore(Store store, String tag) {
        if (store == null) {
            return null;
        }
        if (store.fragment.getTag().equals(tag)) {
            return store;
        }
        if (store.stores == null) {
            return null;
        }
        Store target;
        for (Store child : store.stores) {
            target = findStore(child, tag);
            if (target != null) {
                return target;
            }
        }
        return null;
    }

    @Nullable
    Store findStore(String tag) {
        for (Store value : mStore.values()) {
            Store store = findStore(value, tag);
            if (store != null) {
                return store;
            }
        }
        return null;
    }

    final void add(@NonNull Fragment fragment) {
        Store store = new Store();
        store.fragment = fragment;
        mStore.put(fragment.getTag(), store);
        addFragment(fragment);
    }

    final void add(@NonNull Fragment parentFragment, Fragment fragment) {
        Store store = new Store();
        store.fragment = fragment;
        store.parentStore = findStore(parentFragment.getTag());
        if (store.parentStore != null) {
            if (store.parentStore.stores == null) {
                store.parentStore.stores = new CopyOnWriteArrayList<>();
            }
            store.parentStore.stores.add(store);
        }
        mStore.put(fragment.getTag(), store);
        addFragment(fragment);
    }

    @Nullable
    final Fragment findFragmentByTag(String tag) {
        Store store = findStore(tag);
        return store == null ? null : store.fragment;
    }

    final void remove(@NonNull Fragment fragment) {
        Store store = findStore(fragment.getTag());
        if (store != null) {
            if (store.stores != null) {
                for (Store store1 : store.stores) {
                    remove(store1.fragment);
                }
            }
            if (store.parentStore != null) {
                if (store.parentStore.stores != null) {
                    store.parentStore.stores.remove(store);
                    store.parentStore.stores = null;
                }
                removeFragment(fragment);
            }
            store.fragment = null;
            mStore.remove(fragment.getTag());
        }
    }

    protected void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    protected void removeFragment(Fragment fragment) {
        mFragments.remove(fragment);
    }

    final ArrayList<Fragment> values() {
        return mFragments;
    }

    protected final void clear() {
        for (int i = values().size() - 1; i >= 0; i--) {
            removeFragment(values().get(i));
        }
        mFragments.clear();
        mStore.clear();
    }

    static class Store {
        Store parentStore = null;
        Fragment fragment = null;
        CopyOnWriteArrayList<Store> stores = null;
    }

}
