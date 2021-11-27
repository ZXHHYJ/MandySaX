package mandysax.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

class FragmentStore {

    private final HashMap<String, Store> mStore = new HashMap<>();
    private final CopyOnWriteArrayList<Fragment> mFragments = new CopyOnWriteArrayList<>();

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
    private Store findStore(String tag) {
        for (Store value : mStore.values()) {
            Store store = findStore(value, tag);
            if (store != null) {
                return store;
            }
        }
        return null;
    }

    final void add(@NonNull Fragment newFragment) {
        addFragment(newFragment);
        Store store = new Store();
        store.fragment = newFragment;
        mStore.put(newFragment.getTag(), store);
    }

    final void add(@NonNull Fragment oldFragment, Fragment newFragment) {
        addFragment(newFragment);
        Store store = new Store();
        store.fragment = newFragment;
        store.parentStore = findStore(oldFragment.getTag());
        if (store.parentStore != null) {
            if (store.parentStore.stores == null) {
                store.parentStore.stores = new CopyOnWriteArrayList<>();
            }
            store.parentStore.stores.add(store);
        }
    }

    @Nullable
    final Fragment tagGetFragment(String tag) {
        Store store = findStore(tag);
        return store == null ? null : store.fragment;
    }

    final void remove(@NonNull Fragment fragment) {
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
                store.parentStore.stores.remove(store);
                removeFragment(fragment);
                store.parentStore.stores = null;
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

    final CopyOnWriteArrayList<Fragment> values() {
        return mFragments;
    }

    final void clear() {
        for (int i = values().size() - 1; i >= 0; i--) {
            removeFragment(values().get(i));
        }
        mFragments.clear();
        mStore.clear();
    }

    static class Store {
        Store parentStore;
        Fragment fragment = null;
        CopyOnWriteArrayList<Store> stores = null;
    }

}
