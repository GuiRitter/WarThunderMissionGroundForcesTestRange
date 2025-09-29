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

                Console.WriteLine("Main window found. Searching for the 'Class' combo box:");

                // Find the 'Class' combo box
                var classComboBox = mainWindow.FindFirst(
                    TreeScope.Descendants,
                    new AndCondition(
                        new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ComboBox),
                        new PropertyCondition(AutomationElement.NameProperty, "Class")
                    )
                );

                if (classComboBox == null)
                {
                    Console.WriteLine("The 'Class' combo box was not found.");
                    return;
                }

                Console.WriteLine("'Class' combo box found. Logging details:");
                Console.WriteLine($"  Name: {classComboBox.Current.Name}");
                Console.WriteLine($"  IsEnabled: {classComboBox.Current.IsEnabled}");
                Console.WriteLine($"  BoundingRectangle: {classComboBox.Current.BoundingRectangle}");

                // Log available patterns
                Console.WriteLine("  Available Patterns:");
                foreach (var pattern in classComboBox.GetSupportedPatterns())
                {
                    Console.WriteLine($"    {pattern.ProgrammaticName}");
                }

                // Simulate interaction using InvokePattern
                try
                {
                    if (classComboBox.TryGetCurrentPattern(InvokePattern.Pattern, out object invokePatternObj))
                    {
                        var invokePattern = (InvokePattern)invokePatternObj;
                        invokePattern.Invoke();
                        Console.WriteLine("  Simulated click using InvokePattern.");
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"  Failed to invoke the combo box: {ex.Message}");
                }

                // Expand the combo box to load virtualized items
                try
                {
                    if (classComboBox.TryGetCurrentPattern(ExpandCollapsePattern.Pattern, out object expandPattern))
                    {
                        var expandCollapsePattern = (ExpandCollapsePattern)expandPattern;
                        expandCollapsePattern.Expand();
                        Console.WriteLine("  Expanded the combo box using ExpandCollapsePattern.");
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"  Failed to expand the combo box: {ex.Message}");
                }

                // Retrieve items using FindAll
                var items = classComboBox.FindAll(TreeScope.Children, new PropertyCondition(AutomationElement.ControlTypeProperty, ControlType.ListItem));

                Console.WriteLine($"  Total items (FindAll): {items.Count}");

                // Optionally, list item names
                for (int i = 0; i < items.Count; i++)
                {
                    Console.WriteLine($"    Item {i + 1}: {items[i].Current.Name}");
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
