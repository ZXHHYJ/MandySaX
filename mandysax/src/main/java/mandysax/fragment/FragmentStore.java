package mandysax.fragment;

import java.util.Collection;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

class FragmentStore {

    private final WeakHashMap<String, Store> mStore = new WeakHashMap<>();
    private final Collection<Fragment> mFragments = new CopyOnWriteArrayList<>();

    private Store findStore(Store store, String tag) {
        if (store == null) {
            return null;
        }
        if (store.fragment.getTag().equals(tag)) {
            return store;
        }
        Collection<Store> children = store.stores;
        if (children != null) {
            Store target;
            for (Store child : children) {
                target = findStore(child, tag);
                if (target != null) {
                    return target;
                }
            }
        }
        return null;
    }

    private Store findStore(String tag) {
        for (Store value : mStore.values()) {
            if (value.fragment.getTag().equals(tag)) {
                return value;
            }
        }
        for (Store value : mStore.values()) {
            Store store = findStore(value, tag);
            if (store != null) {
                return store;
            }
        }
        return null;
    }

    final void add(Fragment newFragment) {
        addFragment(newFragment);
        Store store = new Store();
        store.fragment = newFragment;
        mStore.put(newFragment.getTag(), store);
    }

    final void add(Fragment oldFragment, Fragment newFragment) {
        addFragment(newFragment);
        Store store = new Store();
        store.fragment = newFragment;
        Store store1 = findStore(oldFragment.getTag());
        store.parentStore = store1;
        if (store1 != null) {
            if (store1.stores == null) {
                store1.stores = new CopyOnWriteArrayList<>();
            }
            store1.stores.add(store);
        }
    }

    final Fragment tagGetFragment(String tag) {
        Store store = findStore(tag);
        return store == null ? null : store.fragment;
    }

    final void remove(Fragment fragment) {
        Store store = mStore.get(fragment.getTag());
        if (store != null) {
            mStore.remove(fragment.getTag());
            removeFragment(fragment);
            return;
        }
        store = findStore(fragment.getTag());
        if (store != null) {
            if (store.stores != null) {
                for (Store store1 : store.stores) {
                    remove(store1.fragment);
                }
            }
            if (store.parentStore != null) {
                removeFragment(fragment);
                store.parentStore.stores.remove(store);
            }
            store.fragment = null;
        }
    }

    void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    void removeFragment(Fragment fragment) {
        mFragments.remove(fragment);
    }

    final Collection<Fragment> values() {
        return mFragments;
    }

    final void clear() {
        mFragments.clear();
        mStore.clear();
    }

    static class Store {
        Store parentStore;
        Fragment fragment = null;
        Collection<Store> stores = null;
    }

}