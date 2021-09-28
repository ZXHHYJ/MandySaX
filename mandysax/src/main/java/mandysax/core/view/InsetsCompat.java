package mandysax.core.view;


import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author liuxiaoliu66
 */
public final class InsetsCompat implements Parcelable {
    public static final
    InsetsCompat NONE = new InsetsCompat(0, 0, 0, 0);

    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    InsetsCompat(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public static InsetsCompat of(int left, int top, int right, int bottom) {
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return NONE;
        }
        return new InsetsCompat(left, top, right, bottom);
    }

    public static InsetsCompat of(Rect r) {
        return (r == null) ? NONE : of(r.left, r.top, r.right, r.bottom);
    }

    public Rect toRect() {
        return new Rect(left, top, right, bottom);
    }

    public static InsetsCompat add(InsetsCompat a, InsetsCompat b) {
        return InsetsCompat.of(a.left + b.left, a.top + b.top, a.right + b.right, a.bottom + b.bottom);
    }

    public static InsetsCompat subtract(InsetsCompat a, InsetsCompat b) {
        return InsetsCompat.of(a.left - b.left, a.top - b.top, a.right - b.right, a.bottom - b.bottom);
    }

    public static InsetsCompat max(InsetsCompat a, InsetsCompat b) {
        return InsetsCompat.of(Math.max(a.left, b.left), Math.max(a.top, b.top),
                Math.max(a.right, b.right), Math.max(a.bottom, b.bottom));
    }

    public static InsetsCompat min(InsetsCompat a, InsetsCompat b) {
        return InsetsCompat.of(Math.min(a.left, b.left), Math.min(a.top, b.top),
                Math.min(a.right, b.right), Math.min(a.bottom, b.bottom));
    }

    /**
     * Two Insets instances are equal iff they belong to the same class and their fields are
     * pairwise equal.
     *
     * @param o the object to compare this instance with.
     * @return true iff this object is equal {@code o}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InsetsCompat insets = (InsetsCompat) o;

        if (bottom != insets.bottom) {
            return false;
        }
        if (left != insets.left) {
            return false;
        }
        if (right != insets.right) {
            return false;
        }
        return top == insets.top;
    }

    @Override
    public int hashCode() {
        int result = left;
        result = 31 * result + top;
        result = 31 * result + right;
        result = 31 * result + bottom;
        return result;
    }

    @Override
    public String toString() {
        return "InsetsCompat{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(left);
        out.writeInt(top);
        out.writeInt(right);
        out.writeInt(bottom);
    }

    public static final
    Parcelable.Creator<InsetsCompat> CREATOR = new Parcelable.Creator<InsetsCompat>() {
        @Override
        public InsetsCompat createFromParcel(Parcel in) {
            return new InsetsCompat(in.readInt(), in.readInt(), in.readInt(), in.readInt());
        }

        @Override
        public InsetsCompat[] newArray(int size) {
            return new InsetsCompat[size];
        }
    };

}

