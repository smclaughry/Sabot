package org.sabot.client.widget;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.i18n.shared.BidiFormatter;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.i18n.shared.HasDirectionEstimator;
import com.google.gwt.i18n.shared.WordCountDirectionEstimator;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;

public class ListBox extends FocusWidget implements HasChangeHandlers, HasName, HasDirectionEstimator {

  public static final DirectionEstimator DEFAULT_DIRECTION_ESTIMATOR = WordCountDirectionEstimator.get();

  private static final String BIDI_ATTR_NAME = "bidiwrapped";
  
  public static class ItemGroup {
	private final String item;
	private final List<Item> items;
	private final String className;

	public ItemGroup(String item, String className, List<Item> items) {
		this.item = item;
		this.className = className;
		this.items = items;
	}

	public String getItem() {
		return item;
	}

	public List<Item> getItems() {
		return items;
	}

	public String getClassName() {
		return className;
	}
  }
  
  public static class Item {
	private final String item;
	private final Direction dir;
	private final String value;
	private final String className;

	public Item(String item) {
		this(item, item);
	}
		
	public Item(String item, String value) {
		this(item, null, value, null);
	}
	
	public Item(String item, Direction dir, String value, String className) {
		this.item = item;
		this.dir = dir;
		this.value = value;
		this.className = className;
	  }

	public String getItem() {
		return item;
	}

	public Direction getDir() {
		return dir;
	}

	public String getValue() {
		return value;
	}

	public String getClassName() {
		return className;
	}
  }
  
  public static ListBox wrap(Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    ListBox listBox = new ListBox(element);

    // Mark it attached and remember it for cleanup.
    listBox.onAttach();
    RootPanel.detachOnWindowClose(listBox);

    return listBox;
  }

  private DirectionEstimator estimator;

  /**
   * Creates an empty list box in single selection mode.
   */
  public ListBox() {
    this(false);
  }

  /**
   * Creates an empty list box. The preferred way to enable multiple selections
   * is to use this constructor rather than {@link #setMultipleSelect(boolean)}.
   * 
   * @param isMultipleSelect specifies if multiple selection is enabled
   */
  public ListBox(boolean isMultipleSelect) {
    super(Document.get().createSelectElement(isMultipleSelect));
    setStyleName("gwt-ListBox");
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing
   * element. This element must be a &lt;select&gt; element.
   * 
   * @param element the element to be used
   */
  protected ListBox(Element element) {
    super(element);
    SelectElement.as(element);
  }

  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  /**
   * Removes all items from the list box.
   */
  public void clear() {
    //getSelectElement().clear();
    getElement().setInnerHTML("");
  }

  public DirectionEstimator getDirectionEstimator() {
    return estimator;
  }

  /**
   * Gets the number of items present in the list box.
   * 
   * @return the number of items
   */
  public int getItemCount() {
    return getElement().getElementsByTagName("option").getLength();
  }

  /**
   * Gets the text associated with the item at the specified index.
   * 
   * @param index the index of the item whose text is to be retrieved
   * @return the text associated with the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public String getItemText(int index) {
    checkIndex(index);
    return getOptionText(getSelectElement().getOptions().getItem(index));
  }

  public String getName() {
    return getSelectElement().getName();
  }

  /**
   * Gets the currently-selected item. If multiple items are selected, this
   * method will return the first selected item ({@link #isItemSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or <code>-1</code> if none is selected
   */
  public int getSelectedIndex() {
    return getSelectElement().getSelectedIndex();
  }

  /**
   * Gets the value associated with the item at a given index.
   * 
   * @param index the index of the item to be retrieved
   * @return the item's associated value
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public String getValue(int index) {
    checkIndex(index);
    return getSelectElement().getOptions().getItem(index).getValue();
  }

  /**
   * Gets the number of items that are visible. If only one item is visible,
   * then the box will be displayed as a drop-down list.
   * 
   * @return the visible item count
   */
  public int getVisibleItemCount() {
    return getSelectElement().getSize();
  }

  public void addItem(Item item) {
	  insertItem(item.getItem(), item.getDir(), item.getValue(), item.getClassName());
  }
  
  public void addItem(Item item, String optionStyle) {
	  insertItem(item.getItem(), item.getDir(), item.getValue(), optionStyle);
  }
  
  private void insertItem(String item, Direction dir, String value, String optionStyle) {
    SelectElement select = getSelectElement();
    OptionElement option = createOption(item, dir, value);
    if(optionStyle != null) {
    	option.setClassName(optionStyle);
    }
    select.appendChild(option);
  }

  public void addItemGroup(ItemGroup itemGroup) {
	  SelectElement select = getSelectElement();
	  OptGroupElement optGroup = createOptGroupElement(itemGroup.getItem(), itemGroup.getClassName());
	  for(Item item : itemGroup.getItems()) {
		  optGroup.appendChild(createOption(item));
	  }
	  select.appendChild(optGroup);
  }
  
  public void addItemGroups(List<ItemGroup> itemGroups) {
	  for(ItemGroup itemGroup : itemGroups) {
		  addItemGroup(itemGroup);
	  }
  }
  
  private OptionElement createOption(Item item) {
	  return createOption(item.getItem(), item.getDir(), item.getValue(), item.getClassName());
  }
  
  private OptionElement createOption(String item, Direction dir, String value) {
	  return createOption(item, dir, value, null);
  }
  
  
  private OptionElement createOption(String item, Direction dir, String value, String className) {
	  OptionElement option = Document.get().createOptionElement();
	  setOptionText(option, item, dir);
	  option.setValue(value);
	  if(className != null) {
		  option.addClassName(className);
	  }
	  return option;
  }
  
  private OptGroupElement createOptGroupElement(String item, String className) {
	  OptGroupElement optGroup = Document.get().createOptGroupElement();
	  optGroup.setLabel(item);
	  if(className != null) {
		  optGroup.addClassName(className);
	  }
	  return optGroup;
  }
  
  /**
   * Determines whether an individual list item is selected.
   * 
   * @param index the index of the item to be tested
   * @return <code>true</code> if the item is selected
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public boolean isItemSelected(int index) {
    checkIndex(index);
    return getSelectElement().getOptions().getItem(index).isSelected();
  }

  /**
   * Gets whether this list allows multiple selection.
   * 
   * @return <code>true</code> if multiple selection is allowed
   */
  public boolean isMultipleSelect() {
    return getSelectElement().isMultiple();
  }

  /**
   * Removes the item at the specified index.
   *
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void removeItem(int index) {
    checkIndex(index);
    getSelectElement().remove(index);
  }

  /**
   * {@inheritDoc}
   * See note at
   * {@link #setDirectionEstimator(com.google.gwt.i18n.shared.DirectionEstimator)}.
   */
  public void setDirectionEstimator(boolean enabled) {
    setDirectionEstimator(enabled ? DEFAULT_DIRECTION_ESTIMATOR : null);
  }

  /**
   * {@inheritDoc}
   * Note: this does not affect the direction of already-existing content.
   */
  public void setDirectionEstimator(DirectionEstimator directionEstimator) {
    estimator = directionEstimator;
  }

  /**
   * Sets whether an individual list item is selected.
   * 
   * <p>
   * Note that setting the selection programmatically does <em>not</em> cause
   * the {@link ChangeHandler#onChange(ChangeEvent)} event to be fired.
   * </p>
   * 
   * @param index the index of the item to be selected or unselected
   * @param selected <code>true</code> to select the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemSelected(int index, boolean selected) {
    checkIndex(index);
    getSelectElement().getOptions().getItem(index).setSelected(selected);
  }

  /**
   * Sets the text associated with the item at a given index.
   * 
   * @param index the index of the item to be set
   * @param text the item's new text
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemText(int index, String text) {
    setItemText(index, text, null);
  }

  /**
   * Sets the text associated with the item at a given index.
   * 
   * @param index the index of the item to be set
   * @param text the item's new text
   * @param dir the item's direction.
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemText(int index, String text, Direction dir) {
    checkIndex(index);
    if (text == null) {
      throw new NullPointerException("Cannot set an option to have null text");
    }
    setOptionText(getSelectElement().getOptions().getItem(index), text, dir);
  }

  /**
   * Sets whether this list allows multiple selections. <em>NOTE: The preferred
   * way of enabling multiple selections in a list box is by using the
   * {@link #ListBox(boolean)} constructor. Using this method can spuriously
   * fail on Internet Explorer 6.0.</em>
   * 
   * @param multiple <code>true</code> to allow multiple selections
   * @deprecated use {@link #ListBox(boolean)} instead
   */
  @Deprecated
  public void setMultipleSelect(boolean multiple) {
    getSelectElement().setMultiple(multiple);
  }

  public void setName(String name) {
    getSelectElement().setName(name);
  }

  /**
   * Sets the currently selected index.
   * 
   * After calling this method, only the specified item in the list will remain
   * selected. For a ListBox with multiple selection enabled, see
   * {@link #setItemSelected(int, boolean)} to select multiple items at a time.
   * 
   * <p>
   * Note that setting the selected index programmatically does <em>not</em>
   * cause the {@link ChangeHandler#onChange(ChangeEvent)} event to be fired.
   * </p>
   * 
   * @param index the index of the item to be selected
   */
  public void setSelectedIndex(int index) {
    getSelectElement().setSelectedIndex(index);
  }

  /**
   * Sets the value associated with the item at a given index. This value can be
   * used for any purpose, but is also what is passed to the server when the
   * list box is submitted as part of a {@link FormPanel}.
   * 
   * @param index the index of the item to be set
   * @param value the item's new value; cannot be <code>null</code>
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setValue(int index, String value) {
    checkIndex(index);
    getSelectElement().getOptions().getItem(index).setValue(value);
  }

  /**
   * Sets the number of items that are visible. If only one item is visible,
   * then the box will be displayed as a drop-down list.
   * 
   * @param visibleItems the visible item count
   */
  public void setVisibleItemCount(int visibleItems) {
    getSelectElement().setSize(visibleItems);
  }

  /**
   * Retrieves the text of an option element. If the text was set by
   * {@link #setOptionText} and was wrapped with Unicode bidi formatting
   * characters, also removes those additional formatting characters.
   *  
   * @param option an option element
   * @return the element's text
   */
  protected String getOptionText(OptionElement option) {
    String text = option.getText();
    if (option.hasAttribute(BIDI_ATTR_NAME) && text.length() > 1) {
      text = text.substring(1, text.length() - 1);
    }
    return text;
  }

  /**
   * <b>Affected Elements:</b>
   * <ul>
   * <li>-item# = the option at the specified index.</li>
   * </ul>
   * 
   * @see UIObject#onEnsureDebugId(String)
   */
  @Override
  protected void onEnsureDebugId(String baseID) {
    super.onEnsureDebugId(baseID);

    // Set the id of each option
    int numItems = getItemCount();
    for (int i = 0; i < numItems; i++) {
      ensureDebugId(getSelectElement().getOptions().getItem(i), baseID, "item"
          + i);
    }
  }

  /**
   * Sets the text of an option element. If the direction of the text is
   * opposite to the page's direction, also wraps it with Unicode bidi
   * formatting characters to prevent garbling, and indicates that this was done
   * by setting the option's <code>BIDI_ATTR_NAME</code> custom attribute.
   * 
   * @param option an option element
   * @param text text to be set to the element
   * @param dir the text's direction. If {@code null} and direction estimation
   *          is turned off, direction is ignored.
   */
  protected void setOptionText(OptionElement option, String text,
      Direction dir) {
    if (dir == null && estimator != null) {
      dir = estimator.estimateDirection(text);
    }
    if (dir == null) {
      option.setText(text);
      option.removeAttribute(BIDI_ATTR_NAME);
    } else {
      String formattedText =
          BidiFormatter.getInstanceForCurrentLocale().unicodeWrapWithKnownDir(
          dir, text, false /* isHtml */, false /* dirReset */);
      option.setText(formattedText);
      if (formattedText.length() > text.length()) {
        option.setAttribute(BIDI_ATTR_NAME, "");
      } else {
        option.removeAttribute(BIDI_ATTR_NAME);
      }
    }
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException();
    }
  }

  private SelectElement getSelectElement() {
    return getElement().cast();
  }
}
