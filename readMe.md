# Tester App
Create and solve tests to study for your exams.

## What does this thing look like?
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/5d34795f-53a1-46ee-8827-9606a782d96e" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/058bad7f-6072-4d80-ae0d-f10e3376b0d7" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/3dd781fe-e6f9-4439-85e1-45e430f8bf6e" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/337337c8-c8c3-4267-a855-0bd6801a1708" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/4478f2ef-28ae-4cbd-8fa3-bf1bbeee3d98" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/2884185e-0211-45ed-9a51-57704a271b91" />

## Download
[Releases](https://github.com/Selenorius/Tester-App/releases)

## Guide
### Step 1: Create a topic
1. Click on the big *PLUS* button
2. Choose *Create new topic*

### Step 2: Create an exam
1. Click on the *Add exam* button
2. Click on the big *PLUS* button
3. Choose an option

### Step 3: Solve your exam
1. Start **Tester App**.
2. Pick your **Topic**.
3. Pick your **Exam**.

Now you can solve your **Exam**!

## Help
### Image Formats
- .png
- .jpg

### Jar Location
The **Jar** file is in *tester_app/app/build/libs*, but you can also find it in [releases](https://github.com/Selenorius/Tester-App/releases).

### Create an exam by hand
1. Open/create a directory and create a new textfile with the name of your exam.
2. Now open the textfile created in step 1 and add your questions in the following format:

```r
{
    {
        TRUE_FALSE {
            "True/False Question text"
            {
                TRUE
            }
        }
    }
    {
        MULTIPLE_CHOICE ORDERED {
            "Multiple Choice Question text"
            {
                TRUE {
                    "Option 1 text"
                    "Option 1 alternative text"
                }
                FALSE {
                    "Option 2 text"
                }
                FALSE {
                    "Option 3 text"
                }
            }
        }
    }
    {
        WRITTEN ORDERED {
            "Written Question text"
            {
                TRUE {
                    "Option 1 text"
                    "Option 1 alternative text"
                }
                FALSE {
                    "Option 2 text"
                }
                FALSE {
                    "Option 3 text"
                }
            }
        }
    }
}
```

| Enum | Description |
|:--------------- |:-------------------------- |
| MULTIPLE_CHOICE or MC | Multiple Choice Question, can be ORDERED |
| TRUE_FALSE or TF | True/False Question |
| - | Written Question, can be ORDERED |

If **Type** is left empty it will be set to *WRITTEN*.

A **TextOption** has to contain an **Array** of any amount of **String** *text*.

A **ButtonOption** has to contain a **String** *text* and a **Boolean** of value *true* or *false*.

True/False Questions have to contain a **Boolean** of value *true* or *false*.
