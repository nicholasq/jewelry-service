## Entity Relationship Diagram

There are four types of entities:

- Pipeline
    - Meant to group multiple phases together
    - Jobs will move through the various phases of a pipeline
- Phase
    - Holds a collection of jobs
    - The order of the jobs in a phase should matter but is not required
- Job
    - Represents the work that needs to be done
    - Should have at least one contact
- Contact
    - Holds all information about the person
    - Represents the person who is requesting the job.

```mermaid
erDiagram
    PIPELINE ||--o{ PHASE : has
    PIPELINE {
        string id
        string creation_date
        string last_update_date
        string name
        string description
    }
    PHASE ||--o{ JOB : contains
    PHASE {
        string id
        string pipeline_id
        string creation_date
        string last_update_date
        string name
        string description
    }
    JOB ||--o{ CONTACT : contains
    JOB {
        string id
        string phase_id
        string creation_date
        string last_update_date
        string name
        string description
        string priority
        number order
        string notes
    }
    CONTACT {
        string id
        string firstName
        string lastName
        string dateOfBirth
        string email
        string phone
        string address
        string company
        string jobTitle
        string notes
    }
```
