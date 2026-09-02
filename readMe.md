# Tester App
Create and solve tests to study for your exams.

## What does this thing look like?
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/8de9e94f-d8bf-4dd6-abbb-b19933ed9b29" />
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/bfab659f-2a11-432f-bee9-823878d94886" />
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/89f77e30-85d6-43e4-86b8-7d253d252cb8" />
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/11d8228a-93eb-442f-8226-b6833d139163" />
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/43da3417-6bdc-40b9-9c6c-c97135fd3000" />
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/2f627469-e90b-48c2-8ce4-7737e011b8b6" />
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/191e118a-d05b-4892-98a3-f95da44a5bad" />

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
1. Start **Tester App.jar**.
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