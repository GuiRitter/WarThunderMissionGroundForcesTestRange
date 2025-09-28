using System;
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

                        // Expand the combo box to load virtualized items
                        if (comboBox.TryGetCurrentPattern(ExpandCollapsePattern.Pattern, out object pattern))
                        {
                            var expandCollapsePattern = (ExpandCollapsePattern)pattern;
                            expandCollapsePattern.Expand();
                        }

                        // Retrieve items
                        var items = comboBox.FindAll(TreeScope.Children, new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ListItem));

                        Console.WriteLine($"Dropdown {i + 1}: {items.Count} items");

                        // Optionally, list item names
                        for (int j = 0; j < items.Count; j++)
                        {
                            Console.WriteLine($"  Item {j + 1}: {items[j].Current.Name}");
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"An error occurred: {ex.Message}");
            }
        }
    }
}
