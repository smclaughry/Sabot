package org.sabot.client.widget;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SimpleKeyProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link HasConstrainedValue} based on a
 * {@link com.google.gwt.dom.client.SelectElement}.
 * <p>
 * A {@link Renderer Renderer<T>} is used to get user-presentable strings to
 * display in the select element.
 * 
 * @param <T> the value type
 */
public class ValueListBox<T> extends Composite implements HasConstrainedValue<T>, IsEditor<TakesValueEditor<T>>, HasAllFocusHandlers, Focusable  {

  public static class StyledValue<T> {
	  
	private final T value;
	private final String style;

	public StyledValue(T value, String style) {
		this.value = value;
		this.style = style;
	  }

	public T getValue() {
		return value;
	}

	public String getStyle() {
		return style;
	}
  }
	
  public static class ValueGroup<T> {
	private final String label;
	private final String className;
	private final List<T> values;

	public ValueGroup(String label, List<T> values) {
		this(label, null, values);
	}
	
	public ValueGroup(String label, String className, List<T> values) {
		this.label = label;
		this.className = className;
		this.values = values;	  
	}

	public String getLabel() {
		return label;
	}

	public String getClassName() {
		return className;
	}

	public List<T> getValues() {
		return values;
	}
  }
	
  private final List<T> values = new ArrayList<T>();
  private final Map<Object, Integer> valueKeyToIndex = new HashMap<Object, Integer>();
  private final Renderer<T> renderer;
  private final ProvidesKey<T> keyProvider;

  private TakesValueEditor<T> editor;
  private T value;

  public ValueListBox(Renderer<T> renderer) {
    this(renderer, new SimpleKeyProvider<T>());
  }
  
  public ValueListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider) {
    this.keyProvider = keyProvider;
    this.renderer = renderer;
    initWidget(new ListBox());

    getListBox().addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        int selectedIndex = getListBox().getSelectedIndex();

        if (selectedIndex < 0) {
          return; // Not sure why this happens during addValue
        }
        T newValue = values.get(selectedIndex);
        setValue(newValue, true);
      }
    });
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
   * Returns a {@link TakesValueEditor} backed by the ValueListBox.
   */
  public TakesValueEditor<T> asEditor() {
    if (editor == null) {
      editor = TakesValueEditor.of(this);
    }
    return editor;
  }

  public T getValue() {
    return value;
  }

  public void setAcceptableStyledValues(Collection<StyledValue<T>> newValues) {
	    clear();

	    for (StyledValue<T> nextNewValue : newValues) {
	      addValue(nextNewValue);
	    }

	    updateListBox();
  }
  
  public void setAcceptableValues(Collection<T> newValues) {
    clear();

    for (T nextNewValue : newValues) {
      addValue(new StyledValue<T>(nextNewValue, null));
    }

    updateListBox();
  }

  public void setAcceptableValueGroups(Collection<ValueGroup<T>> valueGroups) {
	  clear();
	  for(ValueGroup<T> valueGroup : valueGroups) {
		  addValueGroup(valueGroup);
	  }
	  
	  updateListBox();
  }
  
  private void clear() {
	  values.clear();
	  valueKeyToIndex.clear();
	  ListBox listBox = getListBox();
	  listBox.clear();  
  }
  
  /**
   * Set the value and display it in the select element. Add the value to the
   * acceptable set if it is not already there.
   */
  public void setValue(T value) {
    setValue(value, false);
  }

  public void setValue(T value, boolean fireEvents) {
    if (value == this.value || (this.value != null && this.value.equals(value))) {
      return;
    }

    T before = this.value;
    this.value = value;
    updateListBox();

    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, before, value);
    }
  }

  private void addValueGroup(ValueGroup<T> valueGroup) {
	  ListBox.ItemGroup group = new ListBox.ItemGroup(valueGroup.getLabel(), valueGroup.getClassName(), new ArrayList<ListBox.Item>());
	  for(T value : valueGroup.getValues()) {
		  bookKeep(value);
		  group.getItems().add(new ListBox.Item(renderer.render(value)));
	  }
	  getListBox().addItemGroup(group);
  }
  
  private void addValue(StyledValue<T> value) {
    bookKeep(value.value);
    String label = renderer.render(value.value);
	getListBox().addItem(new ListBox.Item(label, null, label, value.getStyle()));
    assert values.size() == getListBox().getItemCount();
  }

  private void bookKeep(T value) {
	  Object key = keyProvider.getKey(value);
	    if (valueKeyToIndex.containsKey(key)) {
	      throw new IllegalArgumentException("Duplicate value: " + value);
	    }

	    valueKeyToIndex.put(key, values.size());
	    values.add(value);  
  }
  
  public ListBox getListBox() {
    return (ListBox) getWidget();
  }

  private void updateListBox() {
    Object key = keyProvider.getKey(value);
    Integer index = valueKeyToIndex.get(key);
    if (index == null) {
      addValue(new StyledValue<T>(value, null));
    }

    index = valueKeyToIndex.get(key);
    getListBox().setSelectedIndex(index);
  }
  
  @Override
  public HandlerRegistration addFocusHandler(FocusHandler handler) {
      return getListBox().addFocusHandler(handler);
  }

  @Override
  public HandlerRegistration addBlurHandler(BlurHandler handler) {
      return getListBox().addBlurHandler(handler);
  }

  @Override
  public int getTabIndex() {
      return getListBox().getTabIndex();
  }

  @Override
  public void setAccessKey(char key) {
      getListBox().setAccessKey(key);
  }

  @Override
  public void setFocus(boolean focused) {
      getListBox().setFocus(focused);
  }

  @Override
  public void setTabIndex(int index) {
      getListBox().setTabIndex(index);
  }
}
