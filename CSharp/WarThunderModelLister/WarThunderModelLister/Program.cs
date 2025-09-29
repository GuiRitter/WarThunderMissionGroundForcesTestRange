using System;
using System.Diagnostics;
using System.Windows.Automation;

namespace WarThunderModelLister
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Enter the PID of the process:");
            if (!int.TryParse(Console.ReadLine(), out int pid))
            {
                Console.WriteLine("Invalid PID. Please enter a valid number.");
                return;
            }

            Stopwatch stopwatch = Stopwatch.StartNew(); // Start measuring time

            try
            {
                // Find the main window of the process
                AutomationElement mainWindow = AutomationElement.RootElement.FindFirst(
                    TreeScope.Children,
                    new PropertyCondition(AutomationElement.ProcessIdProperty, pid)
                );

                if (mainWindow == null)
                {
                    Console.WriteLine("No main window found for the specified PID.");
                    return;
                }

                Console.WriteLine("Main window found. Listing dropdowns (combo boxes):");

                // Find all combo boxes in the main window
                var comboBoxes = mainWindow.FindAll(
                    TreeScope.Descendants,
                    new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ComboBox)
                );

                if (comboBoxes.Count == 0)
                {
                    Console.WriteLine("No dropdowns (combo boxes) found in the main window.");
                }
                else
                {
                    for (int i = 0; i < comboBoxes.Count; i++)
                    {
                        var comboBox = comboBoxes[i];

                        try
                        {
                            // Log basic properties of the combo box
                            Console.WriteLine($"Dropdown {i + 1}:");
                            Console.WriteLine($"  Name: {comboBox.Current.Name}");
                            Console.WriteLine($"  IsEnabled: {comboBox.Current.IsEnabled}");
                            Console.WriteLine($"  BoundingRectangle: {comboBox.Current.BoundingRectangle}");

                            // Check if the combo box is enabled
                            if (!comboBox.Current.IsEnabled)
                            {
                                Console.WriteLine("  Status: Not enabled.");
                                continue;
                            }

                            // Expand the combo box to load virtualized items
                            if (comboBox.TryGetCurrentPattern(ExpandCollapsePattern.Pattern, out object expandPattern))
                            {
                                var expandCollapsePattern = (ExpandCollapsePattern)expandPattern;
                                expandCollapsePattern.Expand();
                            }

                            // Attempt to use ItemContainerPattern
                            if (comboBox.TryGetCurrentPattern(ItemContainerPattern.Pattern, out object itemContainerPatternObj))
                            {
                                var itemContainerPattern = (ItemContainerPattern)itemContainerPatternObj;
                                AutomationElement item = itemContainerPattern.FindItemByProperty(null, AutomationElement.NameProperty, null);

                                int itemCount = 0;
                                while (item != null)
                                {
                                    Console.WriteLine($"  Item {++itemCount}: {item.Current.Name}");
                                    item = itemContainerPattern.FindItemByProperty(item, AutomationElement.NameProperty, null);
                                }

                                Console.WriteLine($"  Total items: {itemCount}");
                                continue;
                            }

                            // Retrieve items using FindAll
                            var items = comboBox.FindAll(TreeScope.Children, new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ListItem));

                            Console.WriteLine($"  Total items (FindAll): {items.Count}");

                            // Optionally, list item names
                            for (int j = 0; j < items.Count; j++)
                            {
                                Console.WriteLine($"    Item {j + 1}: {items[j].Current.Name}");
                            }
                        }
                        catch (Exception ex)
                        {
                            Console.WriteLine($"  An error occurred: {ex.Message}");
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"An error occurred: {ex.Message}");
            }
            finally
            {
                stopwatch.Stop(); // Stop measuring time
                Console.WriteLine($"Time elapsed: {stopwatch.Elapsed.TotalSeconds} seconds");
            }
        }
    }
}
