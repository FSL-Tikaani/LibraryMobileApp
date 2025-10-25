package com.tikaan.libraryapp.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Декорация для добавления вертикальных отступов между элементами RecyclerView
 * Обеспечивает равномерные промежутки между карточками книг
 */
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_VERTICAL_SPACE = 16; // Значение по умолчанию в dp
    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        // Защита от отрицательных значений - используем значение по умолчанию если переданное некорректно
        this.verticalSpaceHeight = verticalSpaceHeight > 0 ?
                verticalSpaceHeight : DEFAULT_VERTICAL_SPACE;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        // Проверка на валидность позиции
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        // Добавляем верхний отступ только для первого элемента
        if (position == 0) {
            outRect.top = verticalSpaceHeight;
        }

        // Добавляем нижний отступ для всех элементов кроме последнего
        if (position != state.getItemCount() - 1) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}